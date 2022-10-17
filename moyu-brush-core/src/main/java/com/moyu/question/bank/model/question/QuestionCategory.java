package com.moyu.question.bank.model.question;

import lombok.Data;

/**
 * 一个题目的分类：例如按照学科分类
 */
@Data
public class QuestionCategory {
    // 分类名，分类描述
    private String name;
    private String description;

}
