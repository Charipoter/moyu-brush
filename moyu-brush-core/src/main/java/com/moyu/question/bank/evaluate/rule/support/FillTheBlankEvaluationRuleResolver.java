package com.moyu.question.bank.evaluate.rule.support;

import com.google.common.collect.ImmutableMap;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.evaluate.rule.EvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.RuleConstants;

public class FillTheBlankEvaluationRuleResolver implements EvaluationRuleResolver {
    @Override
    public EvaluationRule resolve(String ruleString) {
        FillTheBlankEvaluationRule rule = new FillTheBlankEvaluationRule();

        if (!ruleString.equals(RuleConstants.DEFAULT_NOOP)) {
            // 比例：分
            // n1:m1,n2:m2
            ImmutableMap.Builder<Integer, Float> builder = ImmutableMap.builder();
            String[] parts = ruleString.split(RuleConstants.IN_ITEM_PART_DELIMITER);

            if (parts.length < 1) {
                throw new IllegalArgumentException("填空规则字符串不符合规范，规则项不足");
            }

            for (String part : parts) {
                String[] mapping = part.split(RuleConstants.MAPPING_DELIMITER);

                if (mapping.length != 2) {
                    throw new IllegalArgumentException("填空规则字符串不符合规范，映射规则有误");
                }

                builder.put(Integer.parseInt(mapping[0]), Float.parseFloat(mapping[1]));
            }

            rule.setScoresForCorrectRatio(builder.build());
        }

        return rule;
    }

    @Override
    public boolean support(String idString, String fullString) {
        return idString.equals(RuleConstants.FILL_THE_BLANK_RULE_ID);
    }
}
