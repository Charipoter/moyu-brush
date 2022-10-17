package com.moyu.question.bank.evaluate.rule;

import com.google.common.base.Strings;
import com.moyu.question.bank.evaluate.rule.support.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则解析服务
 */
public class DelegatingEvaluationRuleResolver {

    private static List<EvaluationRuleResolver> resolvers;

    static {
        resolvers = new ArrayList<>();
        resolvers.add(new MultipleChoiceEvaluationResolver());
        resolvers.add(new MixedEvaluationRuleResolver());
        resolvers.add(new FillTheBlankEvaluationRuleResolver());
        resolvers.add(new TrueOrFalseEvaluationRuleResolver());
        resolvers.add(new GenericEvaluationRuleResolver());
    }

    public static EvaluationRule resolve(String fullString) {

        if (Strings.isNullOrEmpty(fullString)) {
            throw new IllegalArgumentException("空字符串");
        }

        String[] sections = fullString.split(RuleConstants.SECTION_DELIMITER);

        if (sections.length < 3) {
            throw new IllegalArgumentException("字符串不符合规范，规则项不足");
        }

        int ruleStringStart = sections[0].length() + sections[1].length() + 2;
        float totalScore = Float.parseFloat(sections[1]);
        String idString = sections[0];

        for (EvaluationRuleResolver resolver : resolvers) {

            if (resolver.support(idString, fullString)) {

                String ruleString = fullString.substring(ruleStringStart);

                if (Strings.isNullOrEmpty(ruleString)) {
                    throw new IllegalArgumentException("字符串不符合规范，ruleString 解析为空");
                }

                EvaluationRule rule = resolver.resolve(ruleString);
                if (rule != null) {
                    // 自动设置总分
                    ((AbstractEvaluationRule) rule).setTotalScore(totalScore);
                    return rule;
                }
            }
        }

        throw new RuntimeException("找不到合适的解析器");
    }

}
