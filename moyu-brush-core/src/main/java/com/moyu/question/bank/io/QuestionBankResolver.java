package com.moyu.question.bank.io;

import com.moyu.question.bank.model.bank.QuestionBank;

import java.io.File;

/**
 * 通过文件解析出题库
 */
public interface QuestionBankResolver {

    QuestionBank resolve(File file);

}
