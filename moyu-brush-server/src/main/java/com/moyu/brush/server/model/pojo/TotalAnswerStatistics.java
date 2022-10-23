package com.moyu.brush.server.model.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 用户的总的答题数据
 */
@Data
@Builder
public class TotalAnswerStatistics {

    private long userId;
    /**
     * 总共获得过的答题分数(以有效模式为准)
     */
    private double totalAnswerScore;
    /**
     * 总共花费的刷题时间，单位是分钟
     */
    private long totalMinutesSpent;

}
