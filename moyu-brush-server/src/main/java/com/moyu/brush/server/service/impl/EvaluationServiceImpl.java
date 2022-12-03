package com.moyu.brush.server.service.impl;

import com.moyu.brush.server.service.EvaluationService;
import com.moyu.brush.server.util.AsyncUtil;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.GenericEvaluator;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    private final GenericEvaluator evaluator = new GenericEvaluator();

    @Override
    public EvaluationResult evaluate(EvaluableQuestion evaluableQuestion, Answer submittedAnswer) {
        return evaluator.evaluate(evaluableQuestion, submittedAnswer);
    }

    @Override
    public List<EvaluationResult> evaluateQuestionBank(QuestionBank questionBank, List<Answer> submittedAnswers) {
        return evaluator.evaluateBatch(questionBank, submittedAnswers);
    }

    @Override
    public List<EvaluationResult> evaluateQuestionBankConcurrently(QuestionBank questionBank,
                                                                   List<Answer> submittedAnswers,
                                                                   ExecutorService executorService) {

        List<EvaluableQuestion> evaluableQuestions = questionBank.getEvaluableQuestions();
        // TODO: 此处并发合理么?
        return AsyncUtil.runBiFunctions(evaluator::evaluate, evaluableQuestions, submittedAnswers, executorService);
    }
}
