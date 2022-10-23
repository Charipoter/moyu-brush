package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.brush.server.model.po.TagPo;

import java.util.List;

/**
* @author lenovo
* @description 针对表【tag】的数据库操作Service
* @createDate 2022-10-12 20:27:14
*/
public interface TagPoService extends IService<TagPo> {

    List<TagPo> getAllByQuestionId(long questionId);

}
