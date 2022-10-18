package com.moyu.brush.server.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName question_bank_item
 */
@TableName(value ="question_bank_item")
@Data
public class QuestionBankItemPO implements Serializable {
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
     * 评分规则字符串
     */
    @TableField(value = "rule_string")
    private String ruleString;

    /**
     * 如果需要，可以在解析后存一份对象
     */
    @TableField(value = "rule_object")
    private String ruleObject;

    /**
     * 所属的题库 id
     */
    @TableField(value = "question_bank_id")
    private Long questionBankId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}