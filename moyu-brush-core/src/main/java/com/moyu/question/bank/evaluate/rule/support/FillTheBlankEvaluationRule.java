package com.moyu.question.bank.evaluate.rule.support;

import com.google.common.collect.ImmutableList;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.AbstractEvaluationRule;
import com.moyu.question.bank.model.question.AnswerOption;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class FillTheBlankEvaluationRule extends AbstractEvaluationRule {
    // 某个匹配度给几分
    private Map<Integer, Float> scoresForCorrectRatio;
    @Override
    protected EvaluationResult doApply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions) {
        // 不能使用不可变集合，否则结果集无法合并
        List<EvaluationResult.OptionResult> optionResults = new ArrayList<>();
        // 此处只允许单题
        if (standardAnswerOptions.size() > 1) {
            throw new IllegalArgumentException("超出判题数量");
        }

        AnswerOption standardAnswerOption = standardAnswerOptions.get(0);
        AnswerOption submittedAnswerOption = submittedAnswerOptions.get(0);

        validateOption(standardAnswerOption, submittedAnswerOption);

        int suitability = computeSuitability(standardAnswerOption.getValue(), submittedAnswerOption.getValue());

        float score;
        if (scoresForCorrectRatio != null && scoresForCorrectRatio.containsKey(suitability)) {
            score = scoresForCorrectRatio.get(suitability);
        } else {
            // 按照比例给分
            score = getTotalScore() * (suitability / 100f);
        }

        optionResults.add(new EvaluationResult.OptionResult(
                ImmutableList.of(standardAnswerOption.getSort()), score, "匹配得分")
        );

        return EvaluationResult.builder().optionResults(optionResults).build();
    }
    /**
     * 解析两个答案的匹配度
     * 匹配度总值为 100
     */
    protected int computeSuitability(String standard, String submitted) {
        return standard.equals(submitted) ? 100 : 0;
    }
}
