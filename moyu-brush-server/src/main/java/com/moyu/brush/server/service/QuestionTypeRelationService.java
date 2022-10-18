package com.moyu.brush.server.service;

import com.moyu.brush.server.model.po.QuestionTypeRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lenovo
* @description 针对表【question_type】的数据库操作Service
* @createDate 2022-10-12 20:27:14
*/
public interface QuestionTypeRelationService extends IService<QuestionTypeRelation> {

    boolean deleteByQuestionId(long questionId);

}
