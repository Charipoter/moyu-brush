package com.moyu.question.bank.evaluate.rule.support;

import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.evaluate.rule.EvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.RuleConstants;

public class TrueOrFalseEvaluationRuleResolver implements EvaluationRuleResolver {
    @Override
    public EvaluationRule resolve(String ruleString) {
        TrueOrFalseEvaluationRule rule = new TrueOrFalseEvaluationRule();
        // (正确得分),(错误得分)
        if (!ruleString.equals(RuleConstants.DEFAULT_NOOP)) {

            String[] parts = ruleString.split(RuleConstants.IN_ITEM_PART_DELIMITER);

            if (parts.length != 2) {
                throw new IllegalArgumentException("判断规则字符串不符合规范，规则项数量不符规范");
            }

            rule.setScoreForCorrect(Float.parseFloat(parts[0]));
            rule.setScoreForIncorrect(Float.parseFloat(parts[1]));
        }

        return rule;
    }

    @Override
    public boolean support(String idString, String fullString) {
        return idString.equals(RuleConstants.TRUE_OR_FALSE_RULE_ID);
    }
}
