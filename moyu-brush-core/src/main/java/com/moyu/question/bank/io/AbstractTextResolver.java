package com.moyu.question.bank.io;

import com.google.common.base.Strings;

import java.io.File;
import java.util.List;

public abstract class AbstractTextResolver extends AbstractQuestionBankResolver {

    private List<ResolveTextStrategy> resolveStrategies;
    @Override
    protected String resolveValidText(File file) {
        String text;
        boolean found = false;

        for (ResolveTextStrategy strategy : resolveStrategies) {
            if (strategy.canResolve(file)) {
                found = true;
                text = strategy.resolve(file);
                if (!Strings.isNullOrEmpty(text)) {
                    return text;
                }
            }
        }

        if (found) {
            throw new IllegalArgumentException("不存在有效的文字");
        } else {
            throw new RuntimeException("找不到合适的处理策略");
        }

    }
}
