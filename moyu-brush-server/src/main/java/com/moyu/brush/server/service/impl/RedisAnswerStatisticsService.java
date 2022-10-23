package com.moyu.brush.server.service.impl;

import com.moyu.brush.server.model.pojo.QuestionBankAnswerRank;
import com.moyu.brush.server.model.pojo.QuestionBankAnswerStatistics;
import com.moyu.brush.server.model.pojo.TotalAnswerRank;
import com.moyu.brush.server.model.pojo.TotalAnswerStatistics;
import com.moyu.brush.server.service.AnswerStatisticsService;
import com.moyu.brush.server.util.AsyncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class RedisAnswerStatisticsService implements AnswerStatisticsService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public static final String TOTAL_STATISTICS_MAP_PREFIX = "totalStatistics";
    // 为保证原子递增，这俩暂时只能拿出来
    public static final String TOTAL_STATISTICS_SCORE_KEY = "score";
    public static final String TOTAL_STATISTICS_TIME_KEY = "time";

    public static final Function<Long, String> TOTAL_STATISTICS_MAP_KEY_GENERATOR =
            (userId) -> TOTAL_STATISTICS_MAP_PREFIX + ":" + userId;
    public static final String BANK_STATISTICS_MAP_PREFIX = "bankStatistics";
    // 为保证原子递增，只能拿出来
    public static final String BANK_STATISTICS_SCORE_KEY = "score";
    public static final String BANK_STATISTICS_TIME_KEY = "time";
    public static final String BANK_STATISTICS_COUNT_KEY = "count";
    public static final BiFunction<Long, Long, String> BANK_STATISTICS_MAP_KEY_GENERATOR =
            (userId, questionBankId) -> BANK_STATISTICS_MAP_PREFIX + ":" + questionBankId + ":" + userId;
    public static final String TOTAL_STATISTICS_SCORE_RANK_KEY = "totalStatisticsScoreRank";
    public static final String TOTAL_STATISTICS_TIME_RANK_KEY = "totalStatisticsTimeRank";
    public static final String BANK_STATISTICS_TIME_RANK_PREFIX = "bankStatisticsTimeRank";
    public static final Function<Long, String> BANK_STATISTICS_TIME_RANK_KEY_GENERATOR =
            (questionBankId) -> BANK_STATISTICS_TIME_RANK_PREFIX + ":" + questionBankId;

    public static final String BANK_STATISTICS_COUNT_RANK_PREFIX = "bankStatisticsCountRank";

    public static final Function<Long, String> BANK_STATISTICS_COUNT_RANK_KEY_GENERATOR =
            (questionBankId) -> BANK_STATISTICS_COUNT_RANK_PREFIX + ":" + questionBankId;
    /**
     * 默认获取多少排名数据
     */
    public static final int defaultSize = 100;

    /**
     * 原子更新最高分的 lua 脚本
     */
    public static final String UPDATE_HIGHEST_SCORE_SCRIPT = "if tonumber(redis.call('hget',KEYS[1],KEYS[2])) < tonumber(ARGV[1]) then return redis.call('hset',KEYS[1],KEYS[2],ARGV[1]) else return 0 end";

    @Override
    public TotalAnswerStatistics getTotalStatistics(long userId) {
        List<Object> rawResults = redisTemplate.opsForHash()
                .multiGet(TOTAL_STATISTICS_MAP_KEY_GENERATOR.apply(userId), List.of(
                        TOTAL_STATISTICS_SCORE_KEY,
                        TOTAL_STATISTICS_TIME_KEY
                ));
        Double score = (Double) rawResults.get(0);
        Long time = (Long) rawResults.get(1);

        return TotalAnswerStatistics.builder()
                .userId(userId)
                .totalAnswerScore(score)
                .totalMinutesSpent(time)
                .build();
    }

    @Override
    public QuestionBankAnswerStatistics getBankStatistics(long userId, long questionBankId) {
        List<Object> rawResults = redisTemplate.opsForHash()
                .multiGet(BANK_STATISTICS_MAP_KEY_GENERATOR.apply(userId, questionBankId), List.of(
                        BANK_STATISTICS_SCORE_KEY,
                        BANK_STATISTICS_TIME_KEY,
                        BANK_STATISTICS_COUNT_KEY
                ));
        Float score = (Float) rawResults.get(0);
        Long time = (Long) rawResults.get(1);
        Long count = (Long) rawResults.get(2);

        return QuestionBankAnswerStatistics.builder()
                .userId(userId)
                .highestScore(score)
                .totalMinutesSpent(time)
                .finishCount(count)
                .build();
    }

    @Override
    public List<TotalAnswerRank.Score> getTotalScoreRank() {
        return getTotalScoreRank(0, defaultSize);
    }

    @Override
    public List<TotalAnswerRank.Score> getTotalScoreRank(int start, int size) {
        Set<ZSetOperations.TypedTuple<Object>> result = redisTemplate.opsForZSet()
                .rangeWithScores(TOTAL_STATISTICS_SCORE_RANK_KEY, start, start + size - 1);

        if (result != null) {
            return result.stream().map(raw ->
                    TotalAnswerRank.Score.builder()
                            .userId((Long) raw.getValue())
                            .totalAnswerScore(raw.getScore())
                            .build()).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<TotalAnswerRank.Time> getTotalTimeRank() {
        return getTotalTimeRank(0, defaultSize);
    }

    @Override
    public List<TotalAnswerRank.Time> getTotalTimeRank(int start, int size) {
        Set<ZSetOperations.TypedTuple<Object>> result = redisTemplate.opsForZSet()
                .rangeWithScores(TOTAL_STATISTICS_TIME_RANK_KEY, start, start + size - 1);

        if (result != null) {
            return result.stream().map(raw ->
                    TotalAnswerRank.Time.builder()
                            .userId((Long) raw.getValue())
                            .totalMinutesSpent(raw.getScore().longValue())
                            .build()).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<QuestionBankAnswerRank.Time> getBankTimeRank(long questionBankId) {
        return getBankTimeRank(questionBankId, 0, defaultSize);
    }

    @Override
    public List<QuestionBankAnswerRank.Time> getBankTimeRank(long questionBankId, int start, int size) {
        Set<ZSetOperations.TypedTuple<Object>> result = redisTemplate.opsForZSet()
                .rangeWithScores(BANK_STATISTICS_TIME_RANK_KEY_GENERATOR.apply(questionBankId), start, start + size - 1);

        if (result != null) {
            return result.stream().map(raw ->
                    QuestionBankAnswerRank.Time.builder()
                            .userId((Long) raw.getValue())
                            .totalMinutesSpent(raw.getScore().longValue())
                            .build()).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<QuestionBankAnswerRank.Count> getBankCountRank(long questionBankId) {
        return getBankCountRank(questionBankId, 0, defaultSize);
    }

    @Override
    public List<QuestionBankAnswerRank.Count> getBankCountRank(long questionBankId, int start, int size) {
        Set<ZSetOperations.TypedTuple<Object>> result = redisTemplate.opsForZSet()
                .rangeWithScores(BANK_STATISTICS_COUNT_RANK_KEY_GENERATOR.apply(questionBankId), start, start + size - 1);

        if (result != null) {
            return result.stream().map(raw ->
                    QuestionBankAnswerRank.Count.builder()
                            .userId((Long) raw.getValue())
                            .finishCount(raw.getScore().longValue())
                            .build()).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateBankScoreIfNecessary(long userId, long questionBankId, float newScore) {
        // 如果分数更高了就更新，使用 lua 脚本
        CompletableFuture<Integer> scoreFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.execute(
                        new DefaultRedisScript<>(UPDATE_HIGHEST_SCORE_SCRIPT, Integer.class), List.of(
                                BANK_STATISTICS_MAP_KEY_GENERATOR.apply(userId, questionBankId),
                                BANK_STATISTICS_SCORE_KEY
                        ), newScore)
        );
        // 不论如何都要更新总数据
        CompletableFuture<Boolean> totalFuture = CompletableFuture.supplyAsync(() ->
                increaseTotalScore(userId, newScore));

        AsyncUtil.runFuturesAnyway(List.of(
                scoreFuture,
                totalFuture
        ));

        return scoreFuture.getNow(0) != 0 && totalFuture.getNow(false);
    }

    @Override
    public boolean increaseBankTime(long userId, long questionBankId, long increasedTime) {
        // 增加时间
        CompletableFuture<Long> timeFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForHash()
                        .increment(
                                BANK_STATISTICS_MAP_KEY_GENERATOR.apply(userId, questionBankId),
                                BANK_STATISTICS_TIME_KEY,
                                increasedTime
                        )
        );
        // 更新排名
        CompletableFuture<Double> rankFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForZSet()
                        .incrementScore(
                                BANK_STATISTICS_TIME_RANK_KEY_GENERATOR.apply(questionBankId),
                                userId,
                                increasedTime
                        )
        );
        AsyncUtil.runFuturesAnyway(List.of(
                timeFuture,
                rankFuture
        ));
        return true;
    }

    @Override
    public boolean increaseBankCount(long userId, long questionBankId, long increasedCount) {
        // 增加次数
        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForHash()
                        .increment(
                                BANK_STATISTICS_MAP_KEY_GENERATOR.apply(userId, questionBankId),
                                BANK_STATISTICS_COUNT_KEY,
                                increasedCount
                        )
        );
        // 更新排名
        CompletableFuture<Double> rankFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForZSet()
                        .incrementScore(
                                BANK_STATISTICS_COUNT_RANK_KEY_GENERATOR.apply(questionBankId),
                                userId,
                                increasedCount
                        )
        );
        AsyncUtil.runFuturesAnyway(List.of(
                countFuture,
                rankFuture
        ));
        return true;
    }

    @Override
    public boolean increaseTotalScore(long userId, float increasedScore) {
        // 增加分数
        CompletableFuture<Double> scoreFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForHash()
                        .increment(
                                TOTAL_STATISTICS_MAP_KEY_GENERATOR.apply(userId),
                                TOTAL_STATISTICS_SCORE_KEY,
                                increasedScore
                        )
        );
        // 更新排名
        CompletableFuture<Double> rankFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForZSet()
                        .incrementScore(
                                TOTAL_STATISTICS_SCORE_RANK_KEY,
                                userId,
                                increasedScore
                        )
        );
        AsyncUtil.runFuturesAnyway(List.of(
                scoreFuture,
                rankFuture
        ));
        return true;
    }

    @Override
    public boolean increaseTotalTime(long userId, long increasedTime) {
        // 增加时间
        CompletableFuture<Long> timeFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForHash()
                        .increment(
                                TOTAL_STATISTICS_MAP_KEY_GENERATOR.apply(userId),
                                TOTAL_STATISTICS_TIME_KEY,
                                increasedTime
                        )
        );
        // 更新排名
        CompletableFuture<Double> rankFuture = CompletableFuture.supplyAsync(() ->
                redisTemplate.opsForZSet()
                        .incrementScore(
                                TOTAL_STATISTICS_TIME_RANK_KEY,
                                userId,
                                increasedTime
                        )
        );
        AsyncUtil.runFuturesAnyway(List.of(
                timeFuture,
                rankFuture
        ));
        return true;
    }
}
