package com.moyu.brush.server.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName question_type
 */
@TableName(value ="question_type")
@Data
@Builder
public class QuestionTypeRelation implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 题目id
     */
    @TableField(value = "question_id")
    private Long questionId;

    /**
     * 
     */
    @TableField(value = "type_id")
    private Integer typeId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}