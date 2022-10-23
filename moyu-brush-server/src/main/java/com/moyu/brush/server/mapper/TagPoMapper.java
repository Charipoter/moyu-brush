package com.moyu.brush.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyu.brush.server.model.po.TagPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author lenovo
* @description 针对表【tag】的数据库操作Mapper
* @createDate 2022-10-12 20:27:14
* @Entity com.moyu.brush.server.model.po.TagPO
*/
public interface TagPoMapper extends BaseMapper<TagPo> {

    List<TagPo> getAllByQuestionId(@Param("questionId") long questionId);

}




