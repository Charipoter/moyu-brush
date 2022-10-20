package com.moyu.brush.server.model.dto;

import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;
import lombok.Data;

/**
 * 前端传递的用于单项判分的对象
 */
@Data
public class QuestionBankItemEvaluationDTO {

    private QuestionBankItem questionBankItem;
    private Answer submittedAnswer;

}
