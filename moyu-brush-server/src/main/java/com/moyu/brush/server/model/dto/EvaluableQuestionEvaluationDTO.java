package com.moyu.brush.server.model.dto;

import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;
import lombok.Data;

/**
 * 前端传递的用于单项判分的对象
 */
@Data
public class EvaluableQuestionEvaluationDTO {

    private EvaluableQuestion evaluableQuestion;
    private Answer submittedAnswer;

}
