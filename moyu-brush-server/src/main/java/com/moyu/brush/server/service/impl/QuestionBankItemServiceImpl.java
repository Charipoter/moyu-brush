package com.moyu.brush.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.po.QuestionBankItemPo;
import com.moyu.brush.server.service.EvaluationService;
import com.moyu.brush.server.service.QuestionBankItemPoService;
import com.moyu.brush.server.service.QuestionBankItemService;
import com.moyu.brush.server.service.QuestionService;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.DelegatingEvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBankItemServiceImpl implements QuestionBankItemService {
    @Autowired
    private QuestionBankItemPoService questionBankItemPoService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private EvaluationService evaluationService;

    @Override
    public QuestionBankItem getById(long questionBankItemId) {
        QuestionBankItemPo questionBankItemPo = questionBankItemPoService.getById(questionBankItemId);
        return toQuestionBankItem(questionBankItemPo);
    }

    @Override
    @Transactional
    public boolean addOne(QuestionBankItemAdditionDTO additionDTO) {
        // 由于 json 多态序列化问题，暂时不在存储时解析对象
        QuestionBankItemPo questionBankItemPo = QuestionBankItemPo.builder()
                .ruleString(additionDTO.getRuleString())
                .ruleObject(JSON.toJSONString(DelegatingEvaluationRuleResolver.resolve(additionDTO.getRuleString())))
                .questionId(additionDTO.getQuestionId())
                .questionBankId(additionDTO.getQuestionBankId())
                .build();

        return questionBankItemPoService.save(questionBankItemPo);
    }

    @Override
    @Transactional
    public boolean addList(List<QuestionBankItemAdditionDTO> additionDTOList) {
        List<QuestionBankItemPo> questionBankItemPoList = additionDTOList.stream().map(
                dto -> QuestionBankItemPo.builder()
                        .ruleString(dto.getRuleString())
                        // 因多态序列化问题，暂时不进行解析
//                        .ruleObject(JSON.toJSONString(DelegatingEvaluationRuleResolver.resolve(dto.getRuleString())))
                        .questionId(dto.getQuestionId())
                        .questionBankId(dto.getQuestionBankId())
                        .build()
        ).toList();

        return questionBankItemPoService.saveBatch(questionBankItemPoList);
    }

    @Override
    public EvaluationResult evaluate(QuestionBankItem questionBankItem, Answer submittedAnswer) {
        return evaluationService.evaluate(questionBankItem, submittedAnswer);
    }

    @Override
    public QuestionBankItem toQuestionBankItem(QuestionBankItemPo questionBankItemPo) {
        return QuestionBankItem.builder()
                .question(questionService.getOneById(questionBankItemPo.getQuestionId()))
                // 这里可以优化，如若对象存在则直接找到对象，但 json 转换存在问题
                .evaluationRule(
                        Strings.isNullOrEmpty(questionBankItemPo.getRuleObject()) 
                                ? DelegatingEvaluationRuleResolver.resolve(questionBankItemPo.getRuleString()) 
                                // JSON 解析的速度如何？
                                : (EvaluationRule) JSON.parse(questionBankItemPo.getRuleObject())
                )
                .build();
    }
}
