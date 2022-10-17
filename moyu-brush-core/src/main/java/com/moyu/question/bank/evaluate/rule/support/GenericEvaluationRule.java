package com.moyu.question.bank.evaluate.rule.support;

import com.google.common.collect.ImmutableList;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.AbstractEvaluationRule;
import com.moyu.question.bank.model.question.AnswerOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenericEvaluationRule extends AbstractEvaluationRule {
    // 对几道给几分
    private Map<Integer, Float> scoreForCorrect;
    @Override
    protected EvaluationResult doApply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions) {
        int correct = 0;
        int optionCount = standardAnswerOptions.size();

        EvaluationResult.OptionResult correctOptionResult = new EvaluationResult.OptionResult();
        EvaluationResult.OptionResult incorrectOptionResult = new EvaluationResult.OptionResult();
        correctOptionResult.setMessage("答对得分");
        incorrectOptionResult.setMessage("答错");

        for (int i = 0; i < optionCount; i++) {

            AnswerOption standardAnswerOption = standardAnswerOptions.get(i);
            AnswerOption submittedAnswerOption = submittedAnswerOptions.get(i);

            validateOption(standardAnswerOption, submittedAnswerOption);

            if (standardAnswerOption.getValue().equals(submittedAnswerOption.getValue())) {
                correct++;
                correctOptionResult.getOptionSorts().add(i);
            } else {
                incorrectOptionResult.getOptionSorts().add(i);
            }
        }

        float score = 0;
        if (scoreForCorrect != null) {
            if (scoreForCorrect.containsKey(correct)) {
                score = scoreForCorrect.get(correct);
            } else if (scoreForCorrect.containsKey(1)) {
                score = scoreForCorrect.get(1) * correct;
            }
        }
        correctOptionResult.setEarnedScore(score);

        return EvaluationResult.builder().optionResults(ImmutableList.of(correctOptionResult, incorrectOptionResult)).build();
    }
}
