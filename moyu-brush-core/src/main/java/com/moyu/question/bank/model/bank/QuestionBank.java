package com.moyu.question.bank.model.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 定义一个题库
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionBank {
    // 一个题库显然拥有一个名称
    private String name;
    // 一个题库的元信息：包含的题目数、创建时间、修改时间、维护者、简介、难度...
    private QuestionBankMetadata metadata;
    // 题库包含的题目项
    private List<QuestionBankItem> questionBankItems;
}
