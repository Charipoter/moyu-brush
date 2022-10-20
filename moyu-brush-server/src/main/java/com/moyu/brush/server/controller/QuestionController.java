package com.moyu.brush.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moyu.brush.server.model.dto.PageDTO;
import com.moyu.brush.server.model.dto.QuestionAdditionDTO;
import com.moyu.brush.server.model.http.R;
import com.moyu.brush.server.service.QuestionService;
import com.moyu.question.bank.model.question.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public R getAll() {
        List<Question> questions = questionService.getAll();
        return R.ok(questions);
    }

    @PostMapping("/page")
    public R getPage(@RequestBody PageDTO pageDTO) {
        Page<Question> page = questionService.getPage(pageDTO);
        return R.ok(page);
    }

    @PostMapping
    public R addOne(@RequestBody QuestionAdditionDTO additionDTO) {
        questionService.addOne(additionDTO);
        return R.ok();
    }

    @DeleteMapping("/id/{questionId}")
    public R deleteById(@PathVariable long questionId) {
        questionService.deleteById(questionId);
        return R.ok();
    }

}
