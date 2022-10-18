package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.model.po.QuestionBankItemPO;
import com.moyu.brush.server.service.QuestionBankItemPOService;
import com.moyu.brush.server.mapper.QuestionBankItemPOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【question_bank_item】的数据库操作Service实现
* @createDate 2022-10-12 23:47:11
*/
@Service
public class QuestionBankItemPOServiceImpl extends ServiceImpl<QuestionBankItemPOMapper, QuestionBankItemPO>
    implements QuestionBankItemPOService{

    @Override
    public List<QuestionBankItemPO> getAllByQuestionBankId(long questionBankId) {
        return list(new LambdaQueryWrapper<QuestionBankItemPO>()
                .eq(QuestionBankItemPO::getQuestionBankId, questionBankId));
    }
}




