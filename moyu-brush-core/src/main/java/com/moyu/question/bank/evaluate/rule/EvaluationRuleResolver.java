package com.moyu.question.bank.evaluate.rule;

/**
 * 根据存储的规则字符串解析出规则
 */
public interface EvaluationRuleResolver {
    EvaluationRule resolve(String ruleString);

    /**
     * 是否支持解析该字符串
     */
    boolean support(String idString, String fullString);

}
