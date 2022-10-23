package com.moyu.brush.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.mapper.QuestionPoMapper;
import com.moyu.brush.server.model.po.QuestionPo;
import com.moyu.brush.server.service.QuestionPoService;
import com.moyu.question.bank.model.question.Question;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【question】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class QuestionPoServiceImpl extends ServiceImpl<QuestionPoMapper, QuestionPo>
    implements QuestionPoService {

    @Override
    public QuestionPo toQuestionPo(Question question) {

        return QuestionPo.builder()
                .answer(JSON.toJSONString(question.getAnswer()))
                .stem(question.getStem())
                .build();

    }

}




