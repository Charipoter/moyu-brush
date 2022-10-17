package com.moyu.question.bank.evaluate.rule.support;

import com.google.common.collect.ImmutableList;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.AbstractEvaluationRule;
import com.moyu.question.bank.model.question.AnswerOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrueOrFalseEvaluationRule extends AbstractEvaluationRule {
    // 选对给多少分
    private Float scoreForCorrect;
    // 选错给多少分
    private Float scoreForIncorrect;
    @Override
    protected EvaluationResult doApply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions) {
        // 最多 2 项
        if (standardAnswerOptions.size() > 2) {
            throw new IllegalArgumentException("判断项过多，可以转换成选择");
        }

        boolean correct = true;

        ImmutableList.Builder<Integer> sortsBuilder = ImmutableList.builder();

        for (int i = 0; i < standardAnswerOptions.size(); i++) {

            AnswerOption standardAnswerOption = standardAnswerOptions.get(i);
            AnswerOption submittedAnswerOption = submittedAnswerOptions.get(i);

            validateOption(standardAnswerOption, submittedAnswerOption);

            if (!standardAnswerOption.getValue().equals(submittedAnswerOption.getValue())) {
                correct = false;
            }

            sortsBuilder.add(standardAnswerOption.getSort());
        }

        float correctScore = scoreForCorrect == null ? getTotalScore() : scoreForCorrect;
        float incorrectScore = scoreForIncorrect == null ? 0 : scoreForIncorrect;

        return EvaluationResult.builder().optionResults(
                List.of(EvaluationResult.OptionResult.builder()
                        .optionSorts(sortsBuilder.build())
                        .earnedScore(correct ? correctScore : incorrectScore)
                        .message("判断")
                        .build()
                )
        ).build();
    }
}
