package com.moyu.question.bank.evaluate.rule.support;

import com.google.common.collect.ImmutableList;
import com.moyu.question.bank.evaluate.rule.DelegatingEvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.evaluate.rule.EvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.RuleConstants;

public class MixedEvaluationRuleResolver implements EvaluationRuleResolver {

    /**
     * (          section-rule         )
     * (n);( full rule)-(n);( full rule)
     */
    @Override
    public EvaluationRule resolve(String ruleString) {

        MixedEvaluationRule rule = new MixedEvaluationRule();
        ImmutableList.Builder<MixedEvaluationRule.Pair> subRulePairsBuilder = ImmutableList.builder();

        String[] mixedSections = ruleString.split(RuleConstants.MIXED_IN_SECTION_SUB_RULE_DELIMITER);

        if (mixedSections.length < 1) {
            throw new IllegalArgumentException("混合规则字符串不符合规范，次要规则不足");
        }

        for (String section : mixedSections) {

            String[] pair = section.split(RuleConstants.MIXED_SCOPE_DELIMITER);
            int scope = Integer.parseInt(pair[0]);
            String subRuleString = pair[1];
            // 委派去解析规则
            EvaluationRule subRule = DelegatingEvaluationRuleResolver.resolve(subRuleString);

            subRulePairsBuilder.add(new MixedEvaluationRule.Pair(subRule, scope));
        }

        rule.setSubRules(subRulePairsBuilder.build());

        return rule;
    }

    @Override
    public boolean support(String idString, String fullString) {
        return idString.equals(RuleConstants.MIXED_RULE_ID);
    }
}
