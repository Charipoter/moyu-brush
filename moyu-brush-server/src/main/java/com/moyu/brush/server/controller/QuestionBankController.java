package com.moyu.brush.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionBankAdditionDTO;
import com.moyu.brush.server.model.dto.QuestionBankEvaluationDTO;
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
    public R evaluateByQuestionBankId(@RequestBody List<Answer> submittedAnswers, @PathVariable long questionBankId) {
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        return R.ok(questionBankService.evaluate(questionBank, submittedAnswers));
    }

    @PostMapping("/evaluate/full")
    public R evaluateByFull(@RequestBody QuestionBankEvaluationDTO evaluationDTO) {
        return R.ok(questionBankService.evaluate(evaluationDTO.getQuestionBank(), evaluationDTO.getSubmittedAnswers()));
    }

    @PostMapping
    public R addOne(@RequestBody QuestionBankAdditionDTO additionDTO) {
        questionBankService.addOne(additionDTO);
        return R.ok();
    }

}
