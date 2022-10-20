package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionBankAdditionDTO;
import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.po.QuestionBankItemPO;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.service.*;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.bank.QuestionBankMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {
    @Autowired
    private QuestionBankItemPOService questionBankItemPOService;
    @Autowired
    private QuestionBankPOService questionBankPOService;
    @Autowired
    private QuestionBankItemService questionBankItemService;
    @Autowired
    private AsyncService asyncService;

    @Override
    public QuestionBank getById(long questionBankId) {

        QuestionBankPO questionBankPO = questionBankPOService.getById(questionBankId);

        return questionBankPO2QuestionBank(questionBankPO);
    }

    @Override
    public Page<QuestionBank> getPage(PageDTO pageDTO) {
        Page<QuestionBankPO> questionBankPOPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize());

        List<QuestionBankPO> questionBankPOList = questionBankPOService.page(questionBankPOPage).getRecords();

        List<QuestionBank> questionBankList = questionBankPOList2QuestionBankList(questionBankPOList);

        Page<QuestionBank> questionBankPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize());
        questionBankPage.setRecords(questionBankList);

        return questionBankPage;
    }

    @Override
    @Transactional
    public boolean addOne(QuestionBankAdditionDTO additionDTO) {
        QuestionBankPO questionBankPO = QuestionBankPO.builder()
                .name(additionDTO.getName())
                .description(additionDTO.getDescription())
                .build();

        questionBankPOService.save(questionBankPO);
        // 拿到 bankId 交给 item
        long questionBankId = questionBankPO.getId();

        List<QuestionBankItemAdditionDTO> questionBankItemAdditionDTOList = additionDTO.getQuestionBankItemAdditionDTOList();
        questionBankItemAdditionDTOList.forEach(dto -> dto.setQuestionBankId(questionBankId));
        // 注意事务传播
        return questionBankItemService.addList(questionBankItemAdditionDTOList);
    }

    @Override
    public QuestionBank questionBankPO2QuestionBank(QuestionBankPO questionBankPO) {

        QuestionBankMetadata metadata = QuestionBankMetadata.builder()
                .createTime(questionBankPO.getCreateTime())
                .updateTime(questionBankPO.getUpdateTime())
                .description(questionBankPO.getDescription())
                .build();
        // 找到包含的题目，解析规则
        List<QuestionBankItemPO> questionBankItemPOList =
                questionBankItemPOService.getAllByQuestionBankId(questionBankPO.getId());

        List<QuestionBankItem> questionBankItems = asyncService.runFunctions(
                questionBankItemService::questionBankItemPO2QuestionBankItem, questionBankItemPOList);

        return QuestionBank.builder()
                .name(questionBankPO.getName())
                .metadata(metadata)
                .questionBankItems(questionBankItems)
                .build();
    }

    @Override
    public List<QuestionBank> questionBankPOList2QuestionBankList(List<QuestionBankPO> questionBankPOList) {
        return asyncService.runFunctions(this::questionBankPO2QuestionBank, questionBankPOList);
    }
}
