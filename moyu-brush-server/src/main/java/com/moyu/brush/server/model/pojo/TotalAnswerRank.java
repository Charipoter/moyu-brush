package com.moyu.brush.server.model.pojo;

import lombok.Builder;
import lombok.Data;

@Data
public class TotalAnswerRank {

    @Data
    @Builder
    public static class Score {

        private long userId;
        /**
         * 总共获得过的答题分数(以有效模式为准)
         */
        private double totalAnswerScore;
    }

    @Data
    @Builder
    public static class Time {

        private long userId;
        /**
         * 总共花费的刷题时间，单位是分钟
         */
        private long totalMinutesSpent;
    }


}
