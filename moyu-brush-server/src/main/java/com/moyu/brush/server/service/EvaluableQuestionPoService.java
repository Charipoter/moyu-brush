package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.brush.server.model.po.EvaluableQuestionPo;

import java.util.List;

/**
* @author lenovo
* @description 针对表【evaluable_question】的数据库操作Service
* @createDate 2022-12-03 18:37:58
*/
public interface EvaluableQuestionPoService extends IService<EvaluableQuestionPo> {

    List<EvaluableQuestionPo> getAllByQuestionBankId(long questionBankId);
}
