package com.moyu.brush.server.service;

import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface EvaluationService {

    EvaluationResult evaluate(EvaluableQuestion evaluableQuestion, Answer submittedAnswer);

    List<EvaluationResult> evaluateQuestionBank(QuestionBank questionBank, List<Answer> submittedAnswers);

    List<EvaluationResult> evaluateQuestionBankConcurrently(QuestionBank questionBank, List<Answer> submittedAnswers, ExecutorService executorService);
}
