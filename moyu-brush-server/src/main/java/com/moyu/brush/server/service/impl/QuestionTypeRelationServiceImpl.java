package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.model.po.QuestionTypeRelation;
import com.moyu.brush.server.service.QuestionTypeRelationService;
import com.moyu.brush.server.mapper.QuestionTypePOMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【question_type】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class QuestionTypeRelationServiceImpl extends ServiceImpl<QuestionTypePOMapper, QuestionTypeRelation>
    implements QuestionTypeRelationService {

    @Override
    public boolean deleteByQuestionId(long questionId) {
        return remove(new LambdaQueryWrapper<QuestionTypeRelation>()
                .eq(QuestionTypeRelation::getQuestionId, questionId)
        );
    }
}




