package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.mapper.TagPOMapper;
import com.moyu.brush.server.model.po.TagPO;
import com.moyu.brush.server.service.TagPOService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class TagPOServiceImpl extends ServiceImpl<TagPOMapper, TagPO>
    implements TagPOService{

    @Override
    public List<TagPO> getAllByQuestionId(long questionId) {
        return baseMapper.getAllByQuestionId(questionId);
    }
}




