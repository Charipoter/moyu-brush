package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionAdditionDTO;
import com.moyu.brush.server.model.po.QuestionPo;
import com.moyu.question.bank.model.question.Question;

import java.util.List;

public interface QuestionService {

    Question getOneById(long id);

    List<Question> getAll();

    Page<Question> getPage(PageDTO pageDTO);

    boolean addOne(QuestionAdditionDTO additionDTO);

    boolean deleteById(long questionId);

    Question toQuestion(QuestionPo questionPO);

    List<Question> toQuestionList(List<QuestionPo> questionPoList);

}
