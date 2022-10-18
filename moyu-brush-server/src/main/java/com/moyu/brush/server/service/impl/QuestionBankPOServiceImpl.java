package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moyu.brush.server.mapper.QuestionBankPOMapper;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.model.vo.PageVO;
import com.moyu.brush.server.service.QuestionBankPOService;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【question_bank】的数据库操作Service实现
* @createDate 2022-10-12 20:32:00
*/
@Service
public class QuestionBankPOServiceImpl extends ServiceImpl<QuestionBankPOMapper, QuestionBankPO>
    implements QuestionBankPOService{

    @Override
    public Page<QuestionBankPO> getPage(PageVO pageVO) {
        Page<QuestionBankPO> page = Page.of(pageVO.getPageIndex(), pageVO.getPageSize());

        return page(page);
    }
}




