package com.moyu.brush.server.model.dto;

import com.moyu.question.bank.model.question.Answer;
import lombok.Data;

import java.util.List;

/**
 * 前端传递的 question 添加对象
 */
@Data
public class QuestionAdditionDTO {
    private String stem;
    private Answer answer;
    private List<Integer> typeIds;
    private List<Long> tagIds;
}
