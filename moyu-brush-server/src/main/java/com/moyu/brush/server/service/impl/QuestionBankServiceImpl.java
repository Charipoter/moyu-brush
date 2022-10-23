package com.moyu.brush.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionBankAdditionDTO;
import com.moyu.brush.server.model.dto.QuestionBankItemAdditionDTO;
import com.moyu.brush.server.model.po.QuestionBankItemPo;
import com.moyu.brush.server.model.po.QuestionBankPo;
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
    private QuestionBankItemPoService questionBankItemPoService;
    @Autowired
    private QuestionBankPoService questionBankPoService;
    @Autowired
    private QuestionBankItemService questionBankItemService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private EvaluationService evaluationService;

    @Override
    public QuestionBank getById(long questionBankId) {

        QuestionBankPo questionBankPo = questionBankPoService.getById(questionBankId);

        return toQuestionBank(questionBankPo);
    }

    @Override
    public Page<QuestionBank> getPage(PageDTO pageDTO) {
        Page<QuestionBankPo> questionBankPoPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize());

        List<QuestionBankPo> questionBankPoList = questionBankPoService.page(questionBankPoPage).getRecords();

        List<QuestionBank> questionBankList = toQuestionBankList(questionBankPoList);

        Page<QuestionBank> questionBankPage = Page.of(pageDTO.getPageIndex(), pageDTO.getPageSize());
        questionBankPage.setRecords(questionBankList);

        return questionBankPage;
    }

    @Override
    @Transactional
    public boolean addOne(QuestionBankAdditionDTO additionDTO) {
        QuestionBankPo questionBankPo = QuestionBankPo.builder()
                .name(additionDTO.getName())
                .description(additionDTO.getDescription())
                .build();

        questionBankPoService.save(questionBankPo);
        // 拿到 bankId 交给 item
        long questionBankId = questionBankPo.getId();

        List<QuestionBankItemAdditionDTO> questionBankItemAdditionDTOList = additionDTO.getQuestionBankItemAdditionDTOList();
        questionBankItemAdditionDTOList.forEach(dto -> dto.setQuestionBankId(questionBankId));
        // 注意事务传播
        return questionBankItemService.addList(questionBankItemAdditionDTOList);
    }

    @Override
    public List<EvaluationResult> evaluate(QuestionBank questionBank, List<Answer> submittedAnswers) {
        if (submittedAnswers.size() >= 100) {
            // 启用并发
            return evaluationService.evaluateQuestionBankConcurrently(questionBank, submittedAnswers, executorService);
        }
        return evaluationService.evaluateQuestionBank(questionBank, submittedAnswers);
    }

    @Override
    public QuestionBank toQuestionBank(QuestionBankPo questionBankPo) {

        QuestionBankMetadata metadata = QuestionBankMetadata.builder()
                .createTime(questionBankPo.getCreateTime())
                .updateTime(questionBankPo.getUpdateTime())
                .description(questionBankPo.getDescription())
                .build();
        // 找到包含的题目，解析规则
        List<QuestionBankItemPo> questionBankItemPoList =
                questionBankItemPoService.getAllByQuestionBankId(questionBankPo.getId());

        List<QuestionBankItem> questionBankItems = AsyncUtil.runFunctions(
                questionBankItemService::toQuestionBankItem, questionBankItemPoList, executorService);

        return QuestionBank.builder()
                .name(questionBankPo.getName())
                .metadata(metadata)
                .questionBankItems(questionBankItems)
                .build();
    }

    @Override
    public List<QuestionBank> toQuestionBankList(List<QuestionBankPo> questionBankPoList) {
        return AsyncUtil.runFunctions(this::toQuestionBank, questionBankPoList, executorService);
    }
}
