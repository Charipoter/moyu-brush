package com.moyu.question.bank.io;

import java.util.List;

/**
 * 有不同的分离出块的策略
 * 例如：按照空格划分
 */
public interface ResolvePlainBlockStrategy {

    List<String> resolve(String text);

}
