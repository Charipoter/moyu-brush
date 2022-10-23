package com.moyu.brush.server.service;

import com.moyu.brush.server.model.pojo.QuestionBankAnswerRank;
import com.moyu.brush.server.model.pojo.QuestionBankAnswerStatistics;
import com.moyu.brush.server.model.pojo.TotalAnswerRank;
import com.moyu.brush.server.model.pojo.TotalAnswerStatistics;

import java.util.List;

/**
 * 维护用户答题数据
 */
public interface AnswerStatisticsService {

    TotalAnswerStatistics getTotalStatistics(long userId);

    QuestionBankAnswerStatistics getBankStatistics(long userId, long questionBankId);

    List<TotalAnswerRank.Score> getTotalScoreRank();

    List<TotalAnswerRank.Score> getTotalScoreRank(int start, int size);
    List<TotalAnswerRank.Time> getTotalTimeRank();

    List<TotalAnswerRank.Time> getTotalTimeRank(int start, int size);
    List<QuestionBankAnswerRank.Time> getBankTimeRank(long questionBankId);

    List<QuestionBankAnswerRank.Time> getBankTimeRank(long questionBankId, int start, int size);

    List<QuestionBankAnswerRank.Count> getBankCountRank(long questionBankId);

    List<QuestionBankAnswerRank.Count> getBankCountRank(long questionBankId, int start, int size);
    /**
     * 如果分数提高了，进行更新
     */
    boolean updateBankScoreIfNecessary(long userId, long questionBankId, float newScore);
    boolean increaseBankTime(long userId, long questionBankId, long increasedTime);

    boolean increaseBankCount(long userId, long questionBankId, long increasedCount);

    boolean increaseTotalScore(long userId, float increasedScore);

    boolean increaseTotalTime(long userId, long increasedTime);

}
