package com.moyu.brush.server.mapper;

import com.moyu.brush.server.model.po.QuestionPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moyu.question.bank.model.question.Question;
import org.apache.ibatis.annotations.Param;

/**
* @author lenovo
* @description 针对表【question】的数据库操作Mapper
* @createDate 2022-10-12 20:27:14
* @Entity com.moyu.brush.server.model.po.QuestionPO
*/
public interface QuestionPOMapper extends BaseMapper<QuestionPO> {

    Question getOneById(@Param("questionId") long questionId);
}




