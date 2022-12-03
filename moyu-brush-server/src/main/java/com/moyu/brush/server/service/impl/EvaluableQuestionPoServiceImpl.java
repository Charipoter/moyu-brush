package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.model.po.EvaluableQuestionPo;
import com.moyu.brush.server.service.EvaluableQuestionPoService;
import com.moyu.brush.server.mapper.EvaluableQuestionPoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【evaluable_question】的数据库操作Service实现
* @createDate 2022-12-03 18:37:58
*/
@Service
public class EvaluableQuestionPoServiceImpl extends ServiceImpl<EvaluableQuestionPoMapper, EvaluableQuestionPo>
    implements EvaluableQuestionPoService{

    @Override
    public List<EvaluableQuestionPo> getAllByQuestionBankId(long questionBankId) {
        return list(new LambdaQueryWrapper<EvaluableQuestionPo>()
                .eq(EvaluableQuestionPo::getQuestionBankId, questionBankId));
    }
}




