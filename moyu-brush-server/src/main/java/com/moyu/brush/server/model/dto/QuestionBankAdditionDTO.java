package com.moyu.brush.server.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 前端传递的完整的题库添加信息
 */
@Data
public class QuestionBankAdditionDTO {
    private String name;
    private String description;
    private List<QuestionBankItemAdditionDTO> questionBankItemAdditionDTOList;
}
