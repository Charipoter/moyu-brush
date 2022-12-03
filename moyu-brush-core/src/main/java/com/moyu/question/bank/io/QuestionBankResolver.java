package com.moyu.question.bank.io;

import com.moyu.question.bank.model.bank.QuestionBank;

import java.io.File;

/**
 * 通过文件解析出题库
 * 思路：
 * 1.由于文件格式不一样，第一层分文件类型，统一处理出有用的文字内容
 * 2.由于文件内容的格式不一样，第二层我们笼统处理出大致分块结构（正则？数据统计？...）
 * 3.对于每个分块结构我们继续第三层分析，尝试解析出题面和问题
 * 4.第四层分析尝试分理出问题
 * 5.第五层进行结构化
 */
public interface QuestionBankResolver {

    /**
     * 解析文件
     * @param file 文件
     * @param allRight 是否要求不出一点差错
     */
    QuestionBank resolve(File file, boolean allRight);

}
