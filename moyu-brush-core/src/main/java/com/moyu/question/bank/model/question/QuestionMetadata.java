package com.moyu.question.bank.model.question;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 题目元信息
 */
@Data
@Builder
public class QuestionMetadata {
    // 题目元信息：创建时间、创建者、更新时间、难度
    private Date createTime;
    private Date updateTime;

}
