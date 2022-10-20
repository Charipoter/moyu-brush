package com.moyu.brush.server.controller;

import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.service.QuestionBankItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questionBankItems")
public class QuestionBankItemController {
    @Autowired
    private QuestionBankItemService questionBankItemService;

    /**
     * TODO：任何时候都允许细粒度添加么？
     */
    @PostMapping
    R addOne(@RequestBody QuestionBankItemAdditionDTO additionDTO) {
        questionBankItemService.addOne(additionDTO);
        return R.ok();
    }

}
