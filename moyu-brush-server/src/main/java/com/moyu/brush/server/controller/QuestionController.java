package com.moyu.brush.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.model.vo.PageVO;
import com.moyu.brush.server.service.QuestionService;
import com.moyu.question.bank.model.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/id/{questionId}")
    public R getById(@PathVariable long questionId) {
        Question question = questionService.getOneById(questionId);
        return R.ok(question);
    }

    @GetMapping
    public R getAll() throws ExecutionException, InterruptedException {
        List<Question> questions = questionService.getAll();
        return R.ok(questions);
    }

    @PostMapping("/page")
    public R getPage(@RequestBody PageVO pageVO) throws ExecutionException, InterruptedException {
        Page<Question> page = questionService.getPage(pageVO);
        return R.ok(page);
    }

    @PostMapping
    public R addOne(@RequestBody Question question) throws ExecutionException, InterruptedException {
        questionService.addOne(question);
        return R.ok();
    }

    @DeleteMapping("/id/{questionId}")
    public R deleteById(@PathVariable long questionId) throws ExecutionException, InterruptedException {
        questionService.deleteById(questionId);
        return R.ok();
    }

}
