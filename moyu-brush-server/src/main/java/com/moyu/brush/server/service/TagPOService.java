package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.brush.server.model.po.TagPO;

import java.util.List;

/**
* @author lenovo
* @description 针对表【tag】的数据库操作Service
* @createDate 2022-10-12 20:27:14
*/
public interface TagPOService extends IService<TagPO> {

    List<TagPO> getAllByQuestionId(long questionId);

}
