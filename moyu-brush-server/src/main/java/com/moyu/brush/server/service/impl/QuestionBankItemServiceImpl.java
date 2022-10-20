package com.moyu.brush.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.moyu.brush.server.component.evaluate.DefaultEvaluator;
import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.po.QuestionBankItemPO;
import com.moyu.brush.server.service.QuestionBankItemPOService;
import com.moyu.brush.server.service.QuestionBankItemService;
import com.moyu.brush.server.service.QuestionService;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.DelegatingEvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBankItemServiceImpl implements QuestionBankItemService {
    @Autowired
    private QuestionBankItemPOService questionBankItemPOService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private DefaultEvaluator evaluator;

    @Override
    public QuestionBankItem getById(long questionBankItemId) {
        QuestionBankItemPO questionBankItemPO = questionBankItemPOService.getById(questionBankItemId);
        return questionBankItemPO2QuestionBankItem(questionBankItemPO);
    }

    @Override
    @Transactional
    public boolean addOne(QuestionBankItemAdditionDTO additionDTO) {
        // 由于 json 多态序列化问题，暂时不在存储时解析对象
        QuestionBankItemPO questionBankItemPO = QuestionBankItemPO.builder()
                .ruleString(additionDTO.getRuleString())
                .ruleObject(JSON.toJSONString(DelegatingEvaluationRuleResolver.resolve(additionDTO.getRuleString())))
                .questionId(additionDTO.getQuestionId())
                .questionBankId(additionDTO.getQuestionBankId())
                .build();

        return questionBankItemPOService.save(questionBankItemPO);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public boolean addList(List<QuestionBankItemAdditionDTO> additionDTOList) {
        List<QuestionBankItemPO> questionBankItemPOList = additionDTOList.stream().map(
                dto -> QuestionBankItemPO.builder()
                        .ruleString(dto.getRuleString())
//                        .ruleObject(JSON.toJSONString(DelegatingEvaluationRuleResolver.resolve(dto.getRuleString())))
                        .questionId(dto.getQuestionId())
                        .questionBankId(dto.getQuestionBankId())
                        .build()
        ).toList();

        return questionBankItemPOService.saveBatch(questionBankItemPOList);
    }

    @Override
    public EvaluationResult evaluate(QuestionBankItem questionBankItem, Answer submittedAnswer) {
        return evaluator.evaluate(questionBankItem, submittedAnswer);
    }

    @Override
    public QuestionBankItem questionBankItemPO2QuestionBankItem(QuestionBankItemPO questionBankItemPO) {
        return QuestionBankItem.builder()
                .question(questionService.getOneById(questionBankItemPO.getQuestionId()))
                // 这里可以优化，如若对象存在则直接找到对象，但 json 转换存在问题
                .evaluationRule(
                        !Strings.isNullOrEmpty(questionBankItemPO.getRuleObject())
                                // JSON 解析的速度如何？
                                ? (EvaluationRule) JSON.parse(questionBankItemPO.getRuleObject())
                                : DelegatingEvaluationRuleResolver.resolve(questionBankItemPO.getRuleString())
                )
                .build();
    }
}
