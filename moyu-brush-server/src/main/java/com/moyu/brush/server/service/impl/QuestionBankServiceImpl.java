package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.model.vo.PageVO;
import com.moyu.brush.server.service.*;
import com.moyu.question.bank.model.bank.QuestionBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {
    @Autowired
    private QuestionBankItemPOService questionBankItemPOService;
    @Autowired
    private QuestionBankPOService questionBankPOService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ConversionService conversionService;
    @Override
    public QuestionBank getById(long questionBankId) {

        QuestionBankPO questionBankPO = questionBankPOService.getById(questionBankId);

        return conversionService.questionBankPO2QuestionBank(questionBankPO);
    }

    @Override
    public Page<QuestionBank> getPage(PageVO pageVO) {
        return null;
    }
}
