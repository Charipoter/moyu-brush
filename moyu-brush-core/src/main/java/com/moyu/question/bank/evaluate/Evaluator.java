package com.moyu.question.bank.evaluate;

import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;

/**
 * 给定题目和回答，评判结果
 */
public interface Evaluator {

    EvaluationResult evaluate(QuestionBankItem question, Answer submittedAnswer);

}
