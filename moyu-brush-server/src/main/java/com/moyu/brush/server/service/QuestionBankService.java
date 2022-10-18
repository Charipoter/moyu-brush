package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.vo.PageVO;
import com.moyu.question.bank.model.bank.QuestionBank;

public interface QuestionBankService {
    QuestionBank getById(long questionBankId);

    Page<QuestionBank> getPage(PageVO pageVO);
}
