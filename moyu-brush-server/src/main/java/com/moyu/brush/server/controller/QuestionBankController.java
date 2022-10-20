package com.moyu.brush.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.component.evaluate.DefaultEvaluator;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionBankAdditionDTO;
import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.service.QuestionBankPOService;
import com.moyu.brush.server.service.QuestionBankService;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questionBanks")
public class QuestionBankController {
    @Autowired
    private QuestionBankPOService questionBankPOService;
    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private DefaultEvaluator evaluator;

    @GetMapping("/basicInfo/page")
    public R getBasicInfoPage(@RequestBody PageDTO pageDTO) {
        Page<QuestionBankPO> page = questionBankPOService.getPage(pageDTO);
        return R.ok(page);
    }

    @GetMapping("/basicInfo/id/{questionBankId}")
    public R getBasicInfoById(@PathVariable long questionBankId) {
        QuestionBankPO questionBankPO = questionBankPOService.getById(questionBankId);
        return R.ok(questionBankPO);
    }

    @GetMapping("/detail/page")
    public R getDetailPage(@RequestBody PageDTO pageDTO) {
        Page<QuestionBank> page = questionBankService.getPage(pageDTO);
        return R.ok(page);
    }

    @GetMapping("/detail/id/{questionBankId}")
    public R getDetailById(@PathVariable long questionBankId) {
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        return R.ok(questionBank);
    }

    @PostMapping("/evaluate/id/{questionBankId}")
    public R evaluateQuestionBankById(@RequestBody List<Answer> submittedAnswers, @PathVariable long questionBankId) {
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        return R.ok(evaluator.evaluateQuestionBank(questionBank, submittedAnswers));
    }

    @PostMapping
    public R addOne(@RequestBody QuestionBankAdditionDTO additionDTO) {
        questionBankService.addOne(additionDTO);
        return R.ok();
    }

}
