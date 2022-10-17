package com.moyu.question.bank.evaluate.rule.support;

import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.evaluate.rule.AbstractEvaluationRule;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.model.question.AnswerOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MixedEvaluationRule extends AbstractEvaluationRule {

    private List<Pair> subRules;
    @Override
    protected EvaluationResult doApply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions) {

        EvaluationResult result = new EvaluationResult();

        int optionIndex = 0, subRuleIndex = 0;
        while (optionIndex < standardAnswerOptions.size()) {
            int scope = subRules.get(subRuleIndex).getScope();
            // 作用的答案为: optionIndex -> optionIndex + scope - 1
            EvaluationResult subResult = subRules.get(subRuleIndex).getRule().apply(
                    standardAnswerOptions.subList(optionIndex, optionIndex + scope),
                    submittedAnswerOptions.subList(optionIndex, optionIndex + scope)
            );
            result.merge(subResult);
            optionIndex += scope;
            subRuleIndex++;
        }

        if (subRuleIndex < subRules.size()) {
            throw new RuntimeException("有未使用的规则，可能是业务异常");
        }

        return result;
    }

    @Data
    @AllArgsConstructor
    public static class Pair {

        private EvaluationRule rule;
        // 作用域大小
        private int scope;

    }
}
