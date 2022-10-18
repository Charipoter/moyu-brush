package com.moyu.brush.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.vo.PageVO;
import com.moyu.question.bank.model.question.Question;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface QuestionService {

    Question getOneById(long id);

    List<Question> getAll() throws ExecutionException, InterruptedException;

    Page<Question> getPage(PageVO pageVO) throws ExecutionException, InterruptedException;

    boolean addOne(Question question) throws ExecutionException, InterruptedException;

    boolean deleteById(long questionId) throws ExecutionException, InterruptedException;

}
