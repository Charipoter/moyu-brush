package com.moyu.question.bank.evaluate.rule;

public class RuleConstants {

    // 规则部件的分隔符
    public static final String SECTION_DELIMITER = "\\|";
    // 部件内部多项的分隔符
    public static final String IN_SECTION_ITEM_DELIMITER = ";";
    // 每一项内部的分隔符
    public static final String IN_ITEM_PART_DELIMITER = ",";
    // 关系符
    public static final String MAPPING_DELIMITER = ":";
    // 混合模式下，规则部件内部的分隔符
    public static final String MIXED_IN_SECTION_SUB_RULE_DELIMITER = "_";
    // 混合模式下，每个规则作用数的分隔符
    public static final String MIXED_SCOPE_DELIMITER = "!";
    // 默认无用的规则，只可用来替代 item 级别
    public static final String DEFAULT_NOOP = "n";


    // 接下来是各规则的 id
    public static final String MULTIPLE_CHOICE_RULE_ID = "multipleChoice";
    public static final String MIXED_RULE_ID = "mixed";
    public static final String FILL_THE_BLANK_RULE_ID = "fillTheBlank";
    public static final String TRUE_OR_FALSE_RULE_ID = "trueOrFalse";

    public static final String GENERIC_RULE_ID = "generic";

}
