package com.moyu.brush.server.service;

import com.moyu.brush.server.model.dto.EvaluationQuestionAdditionDTO;
import com.moyu.brush.server.model.po.EvaluableQuestionPo;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;

import java.util.List;

public interface EvaluableQuestionService {

    EvaluableQuestion getById(long questionBankItemId);

    boolean addOne(EvaluationQuestionAdditionDTO additionDTO);

    boolean addList(List<EvaluationQuestionAdditionDTO> additionDTOList);

    EvaluationResult evaluate(EvaluableQuestion evaluableQuestion, Answer submittedAnswer);
    EvaluableQuestion toQuestionBankItem(EvaluableQuestionPo evaluableQuestionPo);

}
