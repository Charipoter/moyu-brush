package com.moyu.brush.server.controller;

import com.moyu.brush.server.model.dto.EvaluationQuestionAdditionDTO;
import com.moyu.brush.server.model.dto.EvaluableQuestionEvaluationDTO;
import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.service.EvaluableQuestionService;
import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questionBankItems")
public class EvaluableQuestionController {
    @Autowired
    private EvaluableQuestionService evaluableQuestionService;

    @PostMapping("/evaluate/id/{evaluableQuestionId}")
    public R evaluateByQuestionBankItemId(@PathVariable long evaluableQuestionId,
                                          @RequestBody Answer submittedAnswer) {

        EvaluableQuestion evaluableQuestion = evaluableQuestionService.getById(evaluableQuestionId);
        return R.ok(evaluableQuestionService.evaluate(evaluableQuestion, submittedAnswer));
    }

    @PostMapping("/evaluate/full")
    public R evaluateByFull(@RequestBody EvaluableQuestionEvaluationDTO evaluationDTO) {
        return R.ok(evaluableQuestionService.evaluate(evaluationDTO.getEvaluableQuestion(), evaluationDTO.getSubmittedAnswer()));
    }

    /**
     * 任何时候都允许细粒度添加么？
     */
    @PostMapping
    public R addOne(@RequestBody EvaluationQuestionAdditionDTO additionDTO) {
        evaluableQuestionService.addOne(additionDTO);
        return R.ok();
    }

}
