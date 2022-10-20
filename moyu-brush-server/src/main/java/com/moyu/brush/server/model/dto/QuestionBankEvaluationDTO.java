package com.moyu.brush.server.model.dto;

import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.question.Answer;
import lombok.Data;

import java.util.List;

/**
 * 前端传递的用于判分的对象
 */
@Data
public class QuestionBankEvaluationDTO {

    private QuestionBank questionBank;
    private List<Answer> submittedAnswers;

}
