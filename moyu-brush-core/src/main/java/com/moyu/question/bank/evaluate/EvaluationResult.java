package com.moyu.question.bank.evaluate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationResult {
    // 总分
    private float totalScore;
    // 得分
    private float earnedScore;
    // option 结果
    private List<OptionResult> optionResults = new ArrayList<>();

    public void merge(EvaluationResult resultToMerge) {
        earnedScore += resultToMerge.getEarnedScore();
        optionResults.addAll(resultToMerge.getOptionResults());
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionResult {
        // 对于这些选项
        private List<Integer> optionSorts = new ArrayList<>();
        // 得了多少分
        private float earnedScore;
        // 如果需要，此处给定正确答案
        private String message;

        public void append(Integer sort, float deltaScore) {
            optionSorts.add(sort);
            earnedScore += deltaScore;
        }
    }
}
