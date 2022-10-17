package com.moyu.question.bank.model.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 定义一个题目
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {
    // 题干，可以是富文本
    private String stem;
    // 答案
    private Answer answer;
    // 元信息
    private QuestionMetadata metadata;
    // 题目拥有的类别，依据答案选项自动判断（选择、判断）
    private List<QuestionType> types;
    // 题目标签
    private List<QuestionTag> tags;
}
