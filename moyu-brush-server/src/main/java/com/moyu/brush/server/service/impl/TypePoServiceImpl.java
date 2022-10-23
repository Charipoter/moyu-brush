package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.model.po.TypePo;
import com.moyu.brush.server.service.TypePoService;
import com.moyu.brush.server.mapper.TypePoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【type】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class TypePoServiceImpl extends ServiceImpl<TypePoMapper, TypePo>
    implements TypePoService {

    @Override
    public List<TypePo> getAllByQuestionId(long questionId) {
        return baseMapper.getALlByQuestionId(questionId);
    }
}




