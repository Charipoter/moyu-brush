package com.moyu.question.bank.io;

import java.util.List;

public abstract class AbstractPlainBlockResolver extends AbstractTextResolver {

    private List<ResolvePlainBlockStrategy> resolveStrategies;
    @Override
    protected List<String> resolvePlainBlock(String text) {
        List<String> plainBlocks;

        for (ResolvePlainBlockStrategy strategy : resolveStrategies) {
            plainBlocks = strategy.resolve(text);
            if (checkValidPlainBlocks(plainBlocks)) {
                return plainBlocks;
            }
        }

        throw new IllegalArgumentException("无法从文字中解析出 block");
    }

    private boolean checkValidPlainBlocks(List<String> plainBlocks) {
        if (plainBlocks == null || plainBlocks.size() == 0) {
            return false;
        }
        // TODO: 额外逻辑
        return true;
    }
}
