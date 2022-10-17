package com.moyu.question.bank.model.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任何题目的答题都应当可以总结出选项
 *
 * 选择题：请问 xxxxx （）
 *          option1（A）：选了
 *          option2（B）：选了
 *          option3（C）：没选
 *
 * 判断题：请问 xxxxx
 *          option1（对）：选了
 *          option2（错）：没选
 *
 * 填空题：xxx_1_yyy_2_zzz
 *          option1：xxx
 *          option2：yyy
 *
 * 主观题：xxxxxxxxxxxxx
 *          option1：yyyyyyyyyy
 *
 * 选择混合判断：请问 xxxx （），并且 yyy
 *          option1（A）：选了
 *          option2（B）：选了
 *          option3（C）：没选
 *          option4（对）：选了
 *          option5（错）：没选
 *
 * 排序字段是有必要的，用于匹配 option，一般而言，用户提交的回答根据 sort 排序下就可以匹配了
 *
 * 由于高度抽象，选项是不需要指定类型的，选择项也可被当成填空项等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerOption {
    // 排序值，也代表索引值
    private Integer sort;
    // 选项描述，用户提交的可以没有描述
    private String description;
    // 选项答案值
    private String value;
}
