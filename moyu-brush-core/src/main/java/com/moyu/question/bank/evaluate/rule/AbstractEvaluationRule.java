package com.moyu.question.bank.evaluate.rule;

import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.model.question.AnswerOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public abstract class AbstractEvaluationRule implements EvaluationRule {
    // 总分
    protected Float totalScore;

    @Override
    public EvaluationResult apply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions) {
        // 根据规则评判出分数，构造出结果对象
        EvaluationResult result = doApply(standardAnswerOptions, submittedAnswerOptions);

        postProcessAfterApply(result);

        return result;
    }

    protected void postProcessAfterApply(EvaluationResult result) {
        assert result != null;

        result.setTotalScore(totalScore);
        // 计算获得的分数
        float earnedScore = 0;
        for (EvaluationResult.OptionResult optionResult : result.getOptionResults()) {
            earnedScore += optionResult.getEarnedScore();
        }
        result.setEarnedScore(earnedScore);
    }

    protected void validateOption(AnswerOption standardAnswerOption, AnswerOption submittedAnswerOption) {
        if (submittedAnswerOption.getSort() == null || !submittedAnswerOption.getSort().equals(standardAnswerOption.getSort())) {
            throw new IllegalArgumentException("提交的答案排序字段不匹配，无法判分");
        }
    }

    /**
     * 负责解析出 optionResult，即哪些选项贡献了多少分
     */
    protected abstract EvaluationResult doApply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions);
}
