package com.moyu.brush.server.model.dto;

import lombok.Data;

@Data
public class EvaluationQuestionAdditionDTO {
    private long questionId;
    private String ruleString;
    // 属于哪个题库
    private long questionBankId;
}
