package com.moyu.brush.server.model.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 某个用户对某个题库的答题数据统计
 */
@Data
@Builder
public class QuestionBankAnswerStatistics {
    /**
     * 是哪个用户
     */
    private long userId;
    /**
     * 是哪个题库
     */
    private long questionBankId;
    /**
     * 完成次数，以结束为准
     */
    private long finishCount;
    /**
     * 最高得分
     */
    private float highestScore;
    /**
     * 最共刷了多少时间(以完整的开始+结束为准)，单位是分钟
     */
    private long totalMinutesSpent;

}
