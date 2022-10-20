package com.moyu.brush.server.component.evaluate;

import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.GenericEvaluator;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultEvaluator {

    private GenericEvaluator evaluator = new GenericEvaluator();

    public EvaluationResult evaluate(QuestionBankItem questionBankItem, Answer submittedAnswer) {
        return evaluator.evaluate(questionBankItem, submittedAnswer);
    }

    public List<EvaluationResult> evaluateQuestionBank(QuestionBank questionBank, List<Answer> submittedAnswers) {
        return evaluator.evaluateBatch(questionBank, submittedAnswers);
    }

}
