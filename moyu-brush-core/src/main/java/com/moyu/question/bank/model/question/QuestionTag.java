package com.moyu.question.bank.model.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一个题目的标签，用户自定义
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTag {

    private long id;

    private String name;

}
