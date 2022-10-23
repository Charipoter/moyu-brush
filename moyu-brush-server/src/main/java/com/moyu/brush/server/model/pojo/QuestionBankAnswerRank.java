package com.moyu.brush.server.model.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * 某个用户对于题库的答题的用于排名的数据
 */
@Data
public class QuestionBankAnswerRank {

    @Data
    @Builder
    public static class Time {

        private long userId;

        private long totalMinutesSpent;
    }

    @Data
    @Builder
    public static class Count {

        private long userId;

        private long finishCount;

    }

}
