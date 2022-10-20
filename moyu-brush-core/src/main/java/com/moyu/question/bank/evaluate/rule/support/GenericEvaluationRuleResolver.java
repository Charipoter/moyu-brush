package com.moyu.question.bank.evaluate.rule.support;

import com.google.common.collect.ImmutableMap;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.evaluate.rule.EvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.RuleConstants;

public class GenericEvaluationRuleResolver implements EvaluationRuleResolver {
    @Override
    public EvaluationRule resolve(String ruleString) {
        // n:m 选对几道给几分
        GenericEvaluationRule rule = new GenericEvaluationRule();
        ImmutableMap.Builder<Integer, Float> builder = ImmutableMap.builder();

        String[] parts = ruleString.split(RuleConstants.IN_ITEM_PART_DELIMITER);

        if (parts.length == 0) {
            throw new IllegalArgumentException("分数项不足");
        }

        for (String part : parts) {

            if (!part.equals(RuleConstants.DEFAULT_NOOP)) {

                String[] mapping = part.split(RuleConstants.MAPPING_DELIMITER);

                builder.put(Integer.parseInt(mapping[0]), Float.parseFloat(mapping[1]));
            }

        }

        rule.setScoreForCorrect(builder.build());

        return rule;
    }

    @Override
    public boolean support(String idString, String fullString) {
        return idString.equals(RuleConstants.GENERIC_RULE_ID);
    }
}
