package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.model.po.TypePO;
import com.moyu.brush.server.service.TypePOService;
import com.moyu.brush.server.mapper.TypePOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【type】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class TypePOServiceImpl extends ServiceImpl<TypePOMapper, TypePO>
    implements TypePOService{

    @Override
    public List<TypePO> getAllByQuestionId(long questionId) {
        return baseMapper.getALlByQuestionId(questionId);
    }
}




