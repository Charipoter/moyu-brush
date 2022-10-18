package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.mapper.QuestionPOMapper;
import com.moyu.brush.server.model.po.QuestionPO;
import com.moyu.brush.server.service.QuestionPOService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【question】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class QuestionPOServiceImpl extends ServiceImpl<QuestionPOMapper, QuestionPO>
    implements QuestionPOService{
}




