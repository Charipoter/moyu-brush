package com.moyu.question.bank.evaluate.rule.support;

import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.AbstractEvaluationRule;
import com.moyu.question.bank.model.question.AnswerOption;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultipleChoiceEvaluationRule extends AbstractEvaluationRule {
    // 选对几道给多少分
    private Map<Integer, Float> scoreForNumCorrect;
    // 少选几道给多少分
    private Map<Integer, Float> scoreForNumLess;
    // 多选几道给多少分
    private Map<Integer, Float> scoreForNumExceed;
    // 没影响的给多少分
    private Map<Integer, Float> scoreForNumNoImpact;

    private static final String[] messages = new String[]{"正确选择", "多选", "少选", "不影响"};
    private static final int CORRECT = 0;
    private static final int EXCEED = 1;
    private static final int LESS = 2;
    private static final int NO_IMPACT = 3;
    @Override
    protected EvaluationResult doApply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions) {
        // 选对了多少 多选了多少 少选了多少 没影响的
        int correct = 0, exceed = 0, less = 0, noImpact = 0;

        List<EvaluationResult.OptionResult> optionResults = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            EvaluationResult.OptionResult optionResult = new EvaluationResult.OptionResult();
            optionResult.setMessage(messages[i]);
            optionResults.add(optionResult);
        }

        int optionCount = standardAnswerOptions.size();
        for (int i = 0; i < optionCount; i++) {

            AnswerOption standardAnswerOption = standardAnswerOptions.get(i);
            AnswerOption submittedAnswerOption = submittedAnswerOptions.get(i);

            validateOption(standardAnswerOption, submittedAnswerOption);

            boolean needToSelect = resolveValue(standardAnswerOption.getValue());
            boolean selected = resolveValue(submittedAnswerOption.getValue());

            int index;
            if (needToSelect && selected) {
                // 选对了
                correct++;
                index = CORRECT;
            } else if (needToSelect) {
                // 少选
                less++;
                index = LESS;
            } else if (selected) {
                // 多选
                exceed++;
                index = EXCEED;
            } else {
                // 没影响
                noImpact++;
                index = NO_IMPACT;
            }

            optionResults.get(index).getOptionSorts().add(standardAnswerOption.getSort());
        }

        postProcessAfterCount(correct, exceed, less, noImpact, optionResults);

        return EvaluationResult.builder().optionResults(optionResults).build();
    }

    protected void postProcessAfterCount(int correct, int exceed, int less, int noImpact,
                                         List<EvaluationResult.OptionResult> optionResults) {

        applyScoreMap(scoreForNumCorrect, CORRECT, correct, optionResults);
        applyScoreMap(scoreForNumExceed, EXCEED, exceed, optionResults);
        applyScoreMap(scoreForNumLess, LESS, less, optionResults);
        applyScoreMap(scoreForNumNoImpact, NO_IMPACT, noImpact, optionResults);
    }

    private boolean resolveValue(String value) {
        if (value.equals("1")) {
            return true;
        } else if (value.equals("0")) {
            return false;
        } else {
            throw new IllegalArgumentException("选择题值错误");
        }
    }

    private void applyScoreMap(Map<Integer, Float> map, int index, int count,
                               List<EvaluationResult.OptionResult> optionResults) {

        float score = 0;
        if (map != null) {
            if (map.containsKey(count)) {
                score = map.get(count);
            } else if (map.containsKey(1)) {
                // 如果存在 1 的情况，则使用倍数
                score = map.get(1) * count;
            }
        }
        optionResults.get(index).setEarnedScore(score);
    }

    public void setScoreMap(int index, Map<Integer, Float> map) {
        switch (index) {
            case CORRECT -> setScoreForNumCorrect(map);
            case LESS -> setScoreForNumLess(map);
            case EXCEED -> setScoreForNumExceed(map);
            case NO_IMPACT -> setScoreForNumNoImpact(map);
            default -> throw new IllegalArgumentException("错误的 index");
        }
    }

}
