package com.moyu.brush.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.moyu.brush.server.model.po.*;
import com.moyu.brush.server.service.*;
import com.moyu.question.bank.evaluate.rule.DelegatingEvaluationRuleResolver;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.bank.QuestionBankMetadata;
import com.moyu.question.bank.model.question.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Autowired
    private TagPOService tagPOService;
    @Autowired
    private TypePOService typePOService;
    @Autowired
    private QuestionBankItemPOService questionBankItemPOService;
    @Autowired
    private QuestionService questionService;

    @Override
    public Question questionPO2Question(QuestionPO questionPO) {

        QuestionMetadata metadata = QuestionMetadata.builder()
                .createTime(questionPO.getCreateTime())
                .updateTime(questionPO.getUpdateTime())
                .build();

        CompletableFuture<List<TagPO>> tagFuture = CompletableFuture.supplyAsync(() -> tagPOService.getAllByQuestionId(questionPO.getId()));
        CompletableFuture<List<TypePO>> typeFuture = CompletableFuture.supplyAsync(() -> typePOService.getAllByQuestionId(questionPO.getId()));

        Question question = null;

        try {
            CompletableFuture.allOf(tagFuture, typeFuture).get();

            question = Question.builder()
                    .answer(JSON.parseObject(questionPO.getAnswer(), Answer.class))
                    .stem(questionPO.getStem())
                    .metadata(metadata)

                    .tags(tagFuture.get()
                            .stream()
                            .map(po -> QuestionTag.builder()
                                    .id(po.getId())
                                    .name(po.getName())
                                    .build()
                            ).toList()
                    )

                    .types(typeFuture.get()
                            .stream()
                            .map(po -> QuestionType.builder()
                                    .id(po.getId())
                                    .name(po.getName())
                                    .build()
                            ).toList()
                    )

                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return question;
    }

    @Override
    public QuestionPO question2QuestionPO(Question question) {

        return QuestionPO.builder()
                .answer(JSON.toJSONString(question.getAnswer()))
                .stem(question.getStem())
                .build();

    }

    @Override
    public List<Question> questionPOList2QuestionList(List<QuestionPO> questionPOList) throws ExecutionException, InterruptedException {

        if (questionPOList == null || questionPOList.size() == 0) {
            return new ArrayList<>();
        }

        List<CompletableFuture<Question>> futures = new ArrayList<>();

        for (int i = 0; i < questionPOList.size(); i++) {
            final int index = i;
            futures.add(CompletableFuture.supplyAsync(() -> questionPO2Question(questionPOList.get(index))));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

        return futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Override
    public QuestionBank questionBankPO2QuestionBank(QuestionBankPO questionBankPO) {

        QuestionBankMetadata metadata = QuestionBankMetadata.builder()
                .createTime(questionBankPO.getCreateTime())
                .updateTime(questionBankPO.getUpdateTime())
                .description(questionBankPO.getDescription())
                .build();
        // 找到包含的题目，解析规则
        List<QuestionBankItemPO> questionBankItemPOList = questionBankItemPOService.getAllByQuestionBankId(questionBankPO.getId());

        List<QuestionBankItem> questionBankItems = questionBankItemPOList.stream()
                .map(po -> QuestionBankItem.builder()
                        .question(questionService.getOneById(po.getQuestionId()))
                        // 这里可以优化，如若对象存在则直接找到对象
                        .evaluationRule(DelegatingEvaluationRuleResolver.resolve(po.getRuleString()))
                        .build())
                // 并发，如果不明显就手动并发
                .parallel()
                .toList();


        return QuestionBank.builder()
                .name(questionBankPO.getName())
                .metadata(metadata)
                .questionBankItems(questionBankItems)
                .build();
    }


}
