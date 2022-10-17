package com.moyu.question.bank.model.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 题库元信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionBankMetadata {
    // 一个题库的元信息：创建时间、修改时间、维护者、简介、难度...
    private Date createTime;
    private Date updateTime;
    private String description;
}
