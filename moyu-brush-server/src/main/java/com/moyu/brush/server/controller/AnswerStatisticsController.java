package com.moyu.brush.server.controller;

import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.service.AnswerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
public class AnswerStatisticsController {

    @Autowired
    private AnswerStatisticsService answerStatisticsService;

    @GetMapping("/total/{userId}")
    public R getTotalStatistics(@PathVariable Long userId) {
        return R.ok(answerStatisticsService.getTotalStatistics(userId));
    }

    @GetMapping("/bank/{questionBankId}/{userId}")
    public R getBankStatistics(@PathVariable Long questionBankId, @PathVariable Long userId) {
        return R.ok(answerStatisticsService.getBankStatistics(userId, questionBankId));
    }

    @GetMapping("/rank/total/score")
    public R rankWithTotalScore() {
        return R.ok(answerStatisticsService.getTotalScoreRank());
    }

    @GetMapping("/rank/total/time")
    public R rankWithTotalTime() {
        return R.ok(answerStatisticsService.getTotalTimeRank());
    }

    @GetMapping("/rank/bank/{questionBankId}/time")
    public R rankWithBankTime(@PathVariable Long questionBankId) {
        return R.ok(answerStatisticsService.getBankTimeRank(questionBankId));
    }

    @GetMapping("/rank/bank/{questionBankId}/count")
    public R rankWithBankCount(@PathVariable Long questionBankId) {
        return R.ok(answerStatisticsService.getBankCountRank(questionBankId));
    }

    @PostMapping("/rank/total/score")
    public R increaseTotalScore(@RequestParam Long userId, @RequestParam Float increasedScore) {
        answerStatisticsService.increaseTotalScore(userId, increasedScore);
        return R.ok();
    }

    @PostMapping("/rank/total/time")
    public R increaseTotalTime(@RequestParam Long userId, @RequestParam Long increasedTime) {
        answerStatisticsService.increaseTotalTime(userId, increasedTime);
        return R.ok();
    }

    @PostMapping("/rank/bank/{questionBankId}/time")
    public R increaseBankTime(@PathVariable Long questionBankId,
                              @RequestParam Long userId,
                              @RequestParam Long increasedTime) {

        answerStatisticsService.increaseBankTime(userId, questionBankId, increasedTime);
        return R.ok();
    }

    @PostMapping("/rank/bank/{questionBankId}/count")
    public R increaseBankCount(@PathVariable Long questionBankId,
                              @RequestParam Long userId,
                              @RequestParam Long increasedCount) {

        answerStatisticsService.increaseBankCount(userId, questionBankId, increasedCount);
        return R.ok();
    }

    @PostMapping("/bank/{questionBankId}/score")
    public R updateBankHighestScore(@PathVariable Long questionBankId,
                                    @RequestParam Long userId,
                                    @RequestParam Float newScore) {

        answerStatisticsService.updateBankScoreIfNecessary(userId, questionBankId, newScore);
        return R.ok();
    }

}
