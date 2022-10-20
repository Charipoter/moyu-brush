package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionBankAdditionDTO;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.question.Answer;

import java.util.List;

public interface QuestionBankService {
    QuestionBank getById(long questionBankId);

    Page<QuestionBank> getPage(PageDTO pageDTO);

    boolean addOne(QuestionBankAdditionDTO additionDTO);

    List<EvaluationResult> evaluate(QuestionBank questionBank, List<Answer> submittedAnswers);
    QuestionBank questionBankPO2QuestionBank(QuestionBankPO questionBankPO);

    List<QuestionBank> questionBankPOList2QuestionBankList(List<QuestionBankPO> questionBankPOList);
}
