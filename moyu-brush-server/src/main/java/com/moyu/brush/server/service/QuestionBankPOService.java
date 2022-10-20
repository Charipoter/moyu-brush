package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.model.dto.PageDTO;

/**
* @author lenovo
* @description 针对表【question_bank】的数据库操作Service
* @createDate 2022-10-12 20:32:00
*/
public interface QuestionBankPOService extends IService<QuestionBankPO> {

    Page<QuestionBankPO> getPage(PageDTO pageDTO);

}
