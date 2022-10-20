package com.moyu.brush.server.component.evaluate;

import com.moyu.brush.server.util.AsyncUtil;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.GenericEvaluator;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class DefaultEvaluator {
    private GenericEvaluator evaluator = new GenericEvaluator();

    public EvaluationResult evaluate(QuestionBankItem questionBankItem, Answer submittedAnswer) {
        return evaluator.evaluate(questionBankItem, submittedAnswer);
    }

    public List<EvaluationResult> evaluateQuestionBank(QuestionBank questionBank, List<Answer> submittedAnswers) {
        return evaluator.evaluateBatch(questionBank, submittedAnswers);
    }

    public List<EvaluationResult> evaluateQuestionBankConcurrently(QuestionBank questionBank,
                                                                   List<Answer> submittedAnswers,
                                                                   ExecutorService executorService) {

        List<QuestionBankItem> questionBankItems = questionBank.getQuestionBankItems();
        // TODO: 此处并发合理么?
        return AsyncUtil.runBiFunctions(evaluator::evaluate, questionBankItems, submittedAnswers, executorService);
    }

}
