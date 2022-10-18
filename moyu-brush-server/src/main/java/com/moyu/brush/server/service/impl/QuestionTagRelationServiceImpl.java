package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.mapper.QuestionTagPOMapper;
import com.moyu.brush.server.model.po.QuestionTagRelation;
import com.moyu.brush.server.service.QuestionTagRelationService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【question_tag】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class QuestionTagRelationServiceImpl extends ServiceImpl<QuestionTagPOMapper, QuestionTagRelation>
    implements QuestionTagRelationService {

    @Override
    public boolean deleteByQuestionId(long questionId) {
        return remove(new LambdaQueryWrapper<QuestionTagRelation>()
                .eq(QuestionTagRelation::getQuestionId, questionId)
        );
    }
}




