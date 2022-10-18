package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.brush.server.model.po.TypePO;

import java.util.List;

/**
* @author lenovo
* @description 针对表【type】的数据库操作Service
* @createDate 2022-10-12 20:27:14
*/
public interface TypePOService extends IService<TypePO> {

    List<TypePO> getAllByQuestionId(long questionId);

}
