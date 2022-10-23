package com.moyu.brush.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionAdditionDTO;
import com.moyu.brush.server.model.po.*;
import com.moyu.brush.server.service.*;
import com.moyu.brush.server.util.AsyncUtil;
import com.moyu.question.bank.model.question.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionTypeRelationService questionTypeRelationService;
    @Autowired
    private QuestionTagRelationService questionTagRelationService;
    @Autowired
    private QuestionPoService questionPoService;
    @Autowired
    private TypePoService typePoService;
    @Autowired
    private TagPoService tagPoService;
    @Autowired
    private ExecutorService executorService;

    @Override
    public Question getOneById(long id) {
        QuestionPo questionPo = questionPoService.getById(id);

        Question question = null;
        if (questionPo != null) {
            question = toQuestion(questionPo);
        }

        return question;
    }

    @Override
    public List<Question> getAll() {

        List<QuestionPo> questionPoList = questionPoService.list();

        return toQuestionList(questionPoList);
    }

    @Override
    public Page<Question> getPage(PageDTO pageDTO) {

        Page<QuestionPo> questionPoPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize());

        Page<QuestionPo> result = questionPoService.page(questionPoPage);

        List<Question> questionList = toQuestionList(result.getRecords());

        Page<Question> questionPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize(), result.getTotal());
        questionPage.setRecords(questionList);

        return questionPage;
    }


    @Override
    @Transactional
    public boolean addOne(QuestionAdditionDTO additionDTO) {
        QuestionPo questionPo = QuestionPo.builder()
                .stem(additionDTO.getStem()).answer(additionDTO.getStem()).build();

        questionPoService.save(questionPo);

        // 还需要插入 tag、type 相关数据
        List<QuestionTypeRelation> questionTypeRelations = additionDTO.getTypeIds()
                .stream()
                .map(id -> QuestionTypeRelation.builder().questionId(questionPo.getId()).typeId(id).build())
                .toList();

        List<QuestionTagRelation> questionTagRelations = additionDTO.getTagIds()
                .stream()
                .map(id -> QuestionTagRelation.builder().questionId(questionPo.getId()).tagId(id).build())
                .toList();
        // TODO:暂时不保证分布式事务，因此这里不能异步
        return questionTypeRelationService.saveBatch(questionTypeRelations) &&
                questionTagRelationService.saveBatch(questionTagRelations);
    }

    @Override
    @Transactional
    public boolean deleteById(long questionId) {

        questionPoService.removeById(questionId);

        // 还需要移除 type、tag 相关信息
        // TODO:暂时不保证分布式事务，因此这里不能异步
        return questionTypeRelationService.deleteByQuestionId(questionId) &&
                questionTagRelationService.deleteByQuestionId(questionId);
    }

    @Override
    public Question toQuestion(QuestionPo questionPo) {

        QuestionMetadata metadata = QuestionMetadata.builder()
                .createTime(questionPo.getCreateTime())
                .updateTime(questionPo.getUpdateTime())
                .build();

        CompletableFuture<List<TagPo>> tagFuture = CompletableFuture.supplyAsync(
                () -> tagPoService.getAllByQuestionId(questionPo.getId()));

        CompletableFuture<List<TypePo>> typeFuture = CompletableFuture.supplyAsync(
                () -> typePoService.getAllByQuestionId(questionPo.getId()));

        AsyncUtil.runFuturesAnyway(List.of(
                tagFuture,
                typeFuture
        ));

        return Question.builder()
                .answer(JSON.parseObject(questionPo.getAnswer(), Answer.class))
                .stem(questionPo.getStem())
                .metadata(metadata)

                .tags(tagFuture.getNow(null).stream().map(
                        po -> QuestionTag.builder().id(po.getId()).name(po.getName()).build()).toList()
                )
                .types(typeFuture.getNow(null).stream().map(
                        po -> QuestionType.builder().id(po.getId()).name(po.getName()).build()).toList()
                )
                .build();
    }

    @Override
    public List<Question> toQuestionList(List<QuestionPo> questionPoList) {

        if (questionPoList == null || questionPoList.size() == 0) {
            return new ArrayList<>();
        }

        return AsyncUtil.runFunctions(this::toQuestion, questionPoList, executorService);
    }

}
