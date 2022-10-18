package com.moyu.brush.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.model.vo.PageVO;
import com.moyu.brush.server.service.QuestionBankPOService;
import com.moyu.brush.server.service.QuestionBankService;
import com.moyu.question.bank.model.bank.QuestionBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questionBanks")
public class QuestionBankController {

    @Autowired
    private QuestionBankPOService questionBankPOService;
    @Autowired
    private QuestionBankService questionBankService;

    @GetMapping("/basicInfo/page")
    public R getBasicInfoPage(@RequestBody PageVO pageVO) {
        Page<QuestionBankPO> page = questionBankPOService.getPage(pageVO);
        return R.ok(page);
    }

    @GetMapping("/basicInfo/id/{questionBankId}")
    public R getBasicInfoById(@PathVariable long questionBankId) {
        QuestionBankPO questionBankPO = questionBankPOService.getById(questionBankId);
        return R.ok(questionBankPO);
    }

    @GetMapping("/detail/page")
    public R getDetailPage(@RequestBody PageVO pageVO) {
        Page<QuestionBank> page = questionBankService.getPage(pageVO);
        return R.ok(page);
    }

    @GetMapping("/detail/id/{questionBankId}")
    public R getDetailById(@PathVariable long questionBankId) {
        QuestionBank questionBank = questionBankService.getById(questionBankId);
        return R.ok(questionBank);
    }

}
