package com.moyu.brush.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.moyu.brush.server.model.dto.EvaluationQuestionAdditionDTO;
import com.moyu.brush.server.model.po.EvaluableQuestionPo;
import com.moyu.brush.server.service.*;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.DelegatingEvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EvaluableQuestionServiceImpl implements EvaluableQuestionService {
    @Autowired
    private EvaluableQuestionPoService evaluableQuestionPoService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private EvaluationService evaluationService;

    @Override
    public EvaluableQuestion getById(long questionBankItemId) {
        EvaluableQuestionPo evaluableQuestionPo = evaluableQuestionPoService.getById(questionBankItemId);
        return toQuestionBankItem(evaluableQuestionPo);
    }

    @Override
    @Transactional
    public boolean addOne(EvaluationQuestionAdditionDTO additionDTO) {
        // 由于 json 多态序列化问题，暂时不在存储时解析对象
        EvaluableQuestionPo evaluableQuestionPo = EvaluableQuestionPo.builder()
                .ruleString(additionDTO.getRuleString())
                .ruleObject(JSON.toJSONString(DelegatingEvaluationRuleResolver.resolve(additionDTO.getRuleString())))
                .questionId(additionDTO.getQuestionId())
                .questionBankId(additionDTO.getQuestionBankId())
                .build();

        return evaluableQuestionPoService.save(evaluableQuestionPo);
    }

    @Override
    @Transactional
    public boolean addList(List<EvaluationQuestionAdditionDTO> additionDTOList) {
        List<EvaluableQuestionPo> evaluableQuestionPoList = additionDTOList.stream().map(
                dto -> EvaluableQuestionPo.builder()
                        .ruleString(dto.getRuleString())
                        // 因多态序列化问题，暂时不进行解析
//                        .ruleObject(JSON.toJSONString(DelegatingEvaluationRuleResolver.resolve(dto.getRuleString())))
                        .questionId(dto.getQuestionId())
                        .questionBankId(dto.getQuestionBankId())
                        .build()
        ).toList();

        return evaluableQuestionPoService.saveBatch(evaluableQuestionPoList);
    }

    @Override
    public EvaluationResult evaluate(EvaluableQuestion evaluableQuestion, Answer submittedAnswer) {
        return evaluationService.evaluate(evaluableQuestion, submittedAnswer);
    }

    @Override
    public EvaluableQuestion toQuestionBankItem(EvaluableQuestionPo evaluableQuestionPo) {
        return EvaluableQuestion.builder()
                .question(questionService.getOneById(evaluableQuestionPo.getQuestionId()))
                // 这里可以优化，如若对象存在则直接找到对象，但 json 转换存在问题
                .evaluationRule(
                        Strings.isNullOrEmpty(evaluableQuestionPo.getRuleObject())
                                ? DelegatingEvaluationRuleResolver.resolve(evaluableQuestionPo.getRuleString())
                                // JSON 解析的速度如何？
                                : (EvaluationRule) JSON.parse(evaluableQuestionPo.getRuleObject())
                )
                .build();
    }
}
