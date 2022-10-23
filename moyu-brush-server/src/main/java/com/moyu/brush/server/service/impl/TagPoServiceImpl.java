package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.mapper.TagPoMapper;
import com.moyu.brush.server.model.po.TagPo;
import com.moyu.brush.server.service.TagPoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【tag】的数据库操作Service实现
* @createDate 2022-10-12 20:27:14
*/
@Service
public class TagPoServiceImpl extends ServiceImpl<TagPoMapper, TagPo>
    implements TagPoService {

    @Override
    public List<TagPo> getAllByQuestionId(long questionId) {
        return baseMapper.getAllByQuestionId(questionId);
    }
}




