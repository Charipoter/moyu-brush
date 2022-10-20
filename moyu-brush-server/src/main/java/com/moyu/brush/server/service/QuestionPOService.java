package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.brush.server.model.po.QuestionPO;
import com.moyu.question.bank.model.question.Question;

/**
* @author lenovo
* @description 针对表【question】的数据库操作Service
* @createDate 2022-10-12 20:27:14
*/
public interface QuestionPOService extends IService<QuestionPO> {

    QuestionPO question2QuestionPO(Question question);

}
