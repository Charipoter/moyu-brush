package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.brush.server.model.po.QuestionPo;
import com.moyu.question.bank.model.question.Question;

/**
* @author lenovo
* @description 针对表【question】的数据库操作Service
* @createDate 2022-10-12 20:27:14
*/
public interface QuestionPoService extends IService<QuestionPo> {

    QuestionPo toQuestionPo(Question question);

}
