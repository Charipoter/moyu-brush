package com.moyu.brush.server.service;

import com.moyu.brush.server.model.po.QuestionBankItemPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lenovo
* @description 针对表【question_bank_item】的数据库操作Service
* @createDate 2022-10-12 23:47:11
*/
public interface QuestionBankItemPOService extends IService<QuestionBankItemPO> {

    List<QuestionBankItemPO> getAllByQuestionBankId(long questionBankId);
}
