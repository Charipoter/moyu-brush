package com.moyu.question.bank.evaluate.rule.support;

import com.google.common.collect.ImmutableMap;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.evaluate.rule.EvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.RuleConstants;

public class MultipleChoiceEvaluationResolver implements EvaluationRuleResolver {
    @Override
    public EvaluationRule resolve(String ruleString) {
        MultipleChoiceEvaluationRule rule = new MultipleChoiceEvaluationRule();
        // 选对的, 多选的, 少选的, 没影响的
        // n1:m1,n2:m2;n1:m1;n1:m1;n;n
        String[] items = ruleString.split(RuleConstants.IN_SECTION_ITEM_DELIMITER);
        if (items.length < 4) {
            throw new IllegalArgumentException("多选规则字符串不符合规范，规则项不足");
        }

        for (int i = 0; i < 4; i++) {

            if (!items[i].equals(RuleConstants.DEFAULT_NOOP)) {

                ImmutableMap.Builder<Integer, Float> builder = ImmutableMap.builder();
                String[] parts = items[i].split(RuleConstants.IN_ITEM_PART_DELIMITER);

                if (parts.length < 1) {
                    throw new IllegalArgumentException("多选规则字符串不符合规范，映射规则不足");
                }

                for (String part : parts) {
                    String[] mapping = part.split(RuleConstants.MAPPING_DELIMITER);

                    if (mapping.length != 2) {
                        throw new IllegalArgumentException("多选规则字符串不符合规范，映射规则有误");
                    }

                    builder.put(Integer.parseInt(mapping[0]), Float.parseFloat(mapping[1]));
                }

                rule.setScoreMap(i, builder.build());

            }
        }

        return rule;
    }

    @Override
    public boolean support(String idString, String fullString) {
        return idString.equals(RuleConstants.MULTIPLE_CHOICE_RULE_ID);
    }
}
