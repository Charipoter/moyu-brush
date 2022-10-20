package com.moyu.brush.server.controller;

import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.dto.QuestionBankItemEvaluationDTO;
import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.service.QuestionBankItemService;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questionBankItems")
public class QuestionBankItemController {
    @Autowired
    private QuestionBankItemService questionBankItemService;

    @PostMapping("/evaluate/id/{questionBankItemId}")
    public R evaluateByQuestionBankItemId(@PathVariable long questionBankItemId,
                                          @RequestBody Answer submittedAnswer) {

        QuestionBankItem questionBankItem = questionBankItemService.getById(questionBankItemId);
        return R.ok(questionBankItemService.evaluate(questionBankItem, submittedAnswer));
    }

    @PostMapping("/evaluate/full")
    public R evaluateByFull(@RequestBody QuestionBankItemEvaluationDTO evaluationDTO) {
        return R.ok(questionBankItemService.evaluate(evaluationDTO.getQuestionBankItem(), evaluationDTO.getSubmittedAnswer()));
    }

    /**
     * TODO：任何时候都允许细粒度添加么？
     */
    @PostMapping
    public R addOne(@RequestBody QuestionBankItemAdditionDTO additionDTO) {
        questionBankItemService.addOne(additionDTO);
        return R.ok();
    }

}
