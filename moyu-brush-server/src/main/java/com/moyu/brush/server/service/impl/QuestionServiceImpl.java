package com.moyu.brush.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionAdditionDTO;
import com.moyu.brush.server.model.po.*;
import com.moyu.brush.server.service.*;
import com.moyu.question.bank.model.question.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionTypeRelationService questionTypeRelationService;
    @Autowired
    private QuestionTagRelationService questionTagRelationService;
    @Autowired
    private QuestionPOService questionPOService;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private TypePOService typePOService;
    @Autowired
    private TagPOService tagPOService;

    @Override
    public Question getOneById(long id) {
        QuestionPO questionPO = questionPOService.getById(id);

        Question question = null;
        if (questionPO != null) {
            question = questionPO2Question(questionPO);
        }

        return question;
    }

    @Override
    public List<Question> getAll() {

        List<QuestionPO> questionPOList = questionPOService.list();

        return questionPOList2QuestionList(questionPOList);
    }

    @Override
    public Page<Question> getPage(PageDTO pageDTO) {

        Page<QuestionPO> questionPOPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize());

        Page<QuestionPO> result = questionPOService.page(questionPOPage);

        List<Question> questionList = questionPOList2QuestionList(result.getRecords());

        Page<Question> questionPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize(), result.getTotal());
        questionPage.setRecords(questionList);

        return questionPage;
    }


    @Override
    @Transactional
    public boolean addOne(QuestionAdditionDTO additionDTO) {
        QuestionPO questionPO = QuestionPO.builder()
                .stem(additionDTO.getStem()).answer(additionDTO.getStem()).build();

        questionPOService.save(questionPO);

        // 还需要插入 tag、type 相关数据
        List<QuestionTypeRelation> questionTypeRelations = additionDTO.getTypeIds()
                .stream()
                .map(id -> QuestionTypeRelation.builder().questionId(questionPO.getId()).typeId(id).build())
                .toList();

        List<QuestionTagRelation> questionTagRelations = additionDTO.getTagIds()
                .stream()
                .map(id -> QuestionTagRelation.builder().questionId(questionPO.getId()).tagId(id).build())
                .toList();

        CompletableFuture<Boolean> typeFuture = CompletableFuture.supplyAsync(() -> questionTypeRelationService.saveBatch(questionTypeRelations));
        CompletableFuture<Boolean> tagFuture = CompletableFuture.supplyAsync(() -> questionTagRelationService.saveBatch(questionTagRelations));

        try {
            CompletableFuture.allOf(typeFuture, tagFuture).join();
            return typeFuture.get() && tagFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public boolean deleteById(long questionId) {

        questionPOService.removeById(questionId);

        // 还需要移除 type、tag 相关信息
        CompletableFuture<Boolean> typeFuture = CompletableFuture.supplyAsync(() -> questionTypeRelationService.deleteByQuestionId(questionId));
        CompletableFuture<Boolean> tagFuture = CompletableFuture.supplyAsync(() -> questionTagRelationService.deleteByQuestionId(questionId));

        try {
            CompletableFuture.allOf(tagFuture, typeFuture).get();
            return typeFuture.get() && tagFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Question questionPO2Question(QuestionPO questionPO) {

        QuestionMetadata metadata = QuestionMetadata.builder()
                .createTime(questionPO.getCreateTime())
                .updateTime(questionPO.getUpdateTime())
                .build();

        CompletableFuture<List<TagPO>> tagFuture = CompletableFuture.supplyAsync(
                () -> tagPOService.getAllByQuestionId(questionPO.getId()));

        CompletableFuture<List<TypePO>> typeFuture = CompletableFuture.supplyAsync(
                () -> typePOService.getAllByQuestionId(questionPO.getId()));

        asyncService.runFuturesAnyway(List.of(
                tagFuture,
                typeFuture
        ));

        return Question.builder()
                .answer(JSON.parseObject(questionPO.getAnswer(), Answer.class))
                .stem(questionPO.getStem())
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
    public List<Question> questionPOList2QuestionList(List<QuestionPO> questionPOList) {

        if (questionPOList == null || questionPOList.size() == 0) {
            return new ArrayList<>();
        }

        return asyncService.runFunctions(this::questionPO2Question, questionPOList);
    }

}
