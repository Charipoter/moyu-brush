package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.mapper.QuestionBankPoMapper;
import com.moyu.brush.server.model.po.QuestionBankPo;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.service.QuestionBankPoService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【question_bank】的数据库操作Service实现
* @createDate 2022-10-12 20:32:00
*/
@Service
public class QuestionBankPoServiceImpl extends ServiceImpl<QuestionBankPoMapper, QuestionBankPo>
    implements QuestionBankPoService {

    @Override
    public Page<QuestionBankPo> getPage(PageDTO pageDTO) {
        Page<QuestionBankPo> page = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize());

        return page(page);
    }
}




