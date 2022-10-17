package com.moyu.question.bank.evaluate;

import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;
import com.moyu.question.bank.model.question.AnswerOption;

import java.util.List;

public class GenericEvaluator implements Evaluator {
    @Override
    public EvaluationResult evaluate(QuestionBankItem question, Answer submittedAnswer) {
        List<AnswerOption> submittedAnswerOptions = submittedAnswer.getOptions();
        List<AnswerOption> standardAnswerOptions = question.getQuestion().getAnswer().getOptions();

        if (submittedAnswerOptions.size() != standardAnswerOptions.size()) {
            throw new IllegalArgumentException("答案选项数量错误，无法判分");
        }

        return question.getEvaluationRule().apply(standardAnswerOptions, submittedAnswerOptions);
    }


}
