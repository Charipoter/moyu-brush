package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.component.evaluate.DefaultEvaluator;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionBankAdditionDTO;
import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.po.QuestionBankItemPO;
import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.service.*;
import com.moyu.brush.server.util.AsyncUtil;
import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.bank.QuestionBankItem;
import com.moyu.question.bank.model.bank.QuestionBankMetadata;
import com.moyu.question.bank.model.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {
    @Autowired
    private QuestionBankItemPOService questionBankItemPOService;
    @Autowired
    private QuestionBankPOService questionBankPOService;
    @Autowired
    private QuestionBankItemService questionBankItemService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private DefaultEvaluator evaluator;

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
    public List<EvaluationResult> evaluate(QuestionBank questionBank, List<Answer> submittedAnswers) {
        if (submittedAnswers.size() >= 100) {
            // 启用并发
            return evaluator.evaluateQuestionBankConcurrently(questionBank, submittedAnswers, executorService);
        }
        return evaluator.evaluateQuestionBank(questionBank, submittedAnswers);
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

        List<QuestionBankItem> questionBankItems = AsyncUtil.runFunctions(
                questionBankItemService::questionBankItemPO2QuestionBankItem, questionBankItemPOList, executorService);

        return QuestionBank.builder()
                .name(questionBankPO.getName())
                .metadata(metadata)
                .questionBankItems(questionBankItems)
                .build();
    }

    @Override
    public List<QuestionBank> questionBankPOList2QuestionBankList(List<QuestionBankPO> questionBankPOList) {
        return AsyncUtil.runFunctions(this::questionBankPO2QuestionBank, questionBankPOList, executorService);
    }
}
