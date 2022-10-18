package com.moyu.brush.server.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName question_tag
 */
@TableName(value ="question_tag")
@Data
@Builder
public class QuestionTagRelation implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 
     */
    @TableField(value = "question_id")
    private Long questionId;

    /**
     * 
     */
    @TableField(value = "tag_id")
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}