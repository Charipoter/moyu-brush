package com.moyu.brush.server.service;

import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.po.QuestionBankItemPO;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;

import java.util.List;

public interface QuestionBankItemService {

    QuestionBankItem getById(long questionBankItemId);

    boolean addOne(QuestionBankItemAdditionDTO additionDTO);

    boolean addList(List<QuestionBankItemAdditionDTO> additionDTOList);

    EvaluationResult evaluate(QuestionBankItem questionBankItem, Answer submittedAnswer);
    QuestionBankItem questionBankItemPO2QuestionBankItem(QuestionBankItemPO questionBankItemPO);

}
