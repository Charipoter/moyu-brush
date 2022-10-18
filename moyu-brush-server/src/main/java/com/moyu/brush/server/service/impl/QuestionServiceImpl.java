package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.po.QuestionPO;
import com.moyu.brush.server.model.po.QuestionTagRelation;
import com.moyu.brush.server.model.po.QuestionTypeRelation;
import com.moyu.brush.server.model.vo.PageVO;
import com.moyu.brush.server.service.*;
import com.moyu.question.bank.model.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private QuestionTypeRelationService questionTypeRelationService;
    @Autowired
    private QuestionTagRelationService questionTagRelationService;
    @Autowired
    private QuestionPOService questionPOService;

    @Override
    public Question getOneById(long id) {
        QuestionPO questionPO = questionPOService.getById(id);

        Question question = null;
        if (questionPO != null) {
            question = conversionService.questionPO2Question(questionPO);
        }

        return question;
    }

    @Override
    public List<Question> getAll() throws ExecutionException, InterruptedException {
        List<QuestionPO> questionPOList = questionPOService.list();

        return conversionService.questionPOList2QuestionList(questionPOList);
    }

    @Override
    public Page<Question> getPage(PageVO pageVO) throws ExecutionException, InterruptedException {
        Page<QuestionPO> questionPOPage = Page.of(pageVO.getPageIndex(), pageVO.getPageSize());

        Page<QuestionPO> result = questionPOService.page(questionPOPage);

        List<Question> questionList = conversionService.questionPOList2QuestionList(result.getRecords());

        Page<Question> questionPage = Page.of(pageVO.getPageIndex(), pageVO.getPageSize(), result.getTotal());
        questionPage.setRecords(questionList);

        return questionPage;
    }


    @Override
    @Transactional
    public boolean addOne(Question question) throws ExecutionException, InterruptedException {
        QuestionPO questionPO = conversionService.question2QuestionPO(question);

        questionPOService.save(questionPO);

        // 还需要插入 tag、type 相关数据
        List<QuestionTypeRelation> questionTypeRelations = question.getTypes()
                .stream()
                .map(type -> QuestionTypeRelation.builder().questionId(questionPO.getId()).typeId(type.getId()).build())
                .toList();

        List<QuestionTagRelation> questionTagRelations = question.getTags()
                .stream()
                .map(tag -> QuestionTagRelation.builder().questionId(questionPO.getId()).tagId(tag.getId()).build())
                .toList();

        CompletableFuture<Boolean> typeFuture = CompletableFuture.supplyAsync(() -> questionTypeRelationService.saveBatch(questionTypeRelations));
        CompletableFuture<Boolean> tagFuture = CompletableFuture.supplyAsync(() -> questionTagRelationService.saveBatch(questionTagRelations));

        CompletableFuture.allOf(typeFuture, tagFuture).get();

        return typeFuture.get() && tagFuture.get();
    }

    @Override
    @Transactional
    public boolean deleteById(long questionId) throws ExecutionException, InterruptedException {
        questionPOService.removeById(questionId);

        // 还需要移除 type、tag 相关信息
        CompletableFuture<Boolean> typeFuture = CompletableFuture.supplyAsync(() -> questionTypeRelationService.deleteByQuestionId(questionId));
        CompletableFuture<Boolean> tagFuture = CompletableFuture.supplyAsync(() -> questionTagRelationService.deleteByQuestionId(questionId));

        CompletableFuture.allOf(tagFuture, typeFuture).get();

        return typeFuture.get() && tagFuture.get();
    }

}
