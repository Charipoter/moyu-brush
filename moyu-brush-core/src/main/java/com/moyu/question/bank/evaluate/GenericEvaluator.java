package com.moyu.question.bank.evaluate;

import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;
import com.moyu.question.bank.model.question.AnswerOption;

import java.util.ArrayList;
import java.util.List;

public class GenericEvaluator implements Evaluator {
    @Override
    public EvaluationResult evaluate(EvaluableQuestion evaluableQuestion, Answer submittedAnswer) {
        List<AnswerOption> submittedAnswerOptions = submittedAnswer.getOptions();
        List<AnswerOption> standardAnswerOptions = evaluableQuestion.getQuestion().getAnswer().getOptions();

        if (submittedAnswerOptions.size() != standardAnswerOptions.size()) {
            throw new IllegalArgumentException("答案选项数量错误，无法判分");
        }

        return evaluableQuestion.getEvaluationRule().apply(standardAnswerOptions, submittedAnswerOptions);
    }

    public List<EvaluationResult> evaluateBatch(QuestionBank questionBank, List<Answer> submittedAnswers) {

        List<EvaluableQuestion> evaluableQuestions = questionBank.getEvaluableQuestions();

        if (evaluableQuestions.size() != submittedAnswers.size()) {
            throw new IllegalArgumentException("答案数量错误，无法判分");
        }

        List<EvaluationResult> results = new ArrayList<>();
        for (int i = 0; i < evaluableQuestions.size(); i++) {
            results.add(evaluate(evaluableQuestions.get(i), submittedAnswers.get(i)));
        }

        return results;
    }

}
