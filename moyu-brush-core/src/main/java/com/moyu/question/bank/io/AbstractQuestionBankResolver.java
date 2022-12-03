package com.moyu.question.bank.io;

import com.moyu.question.bank.model.bank.QuestionBank;

import java.io.File;
import java.util.List;
import java.util.Objects;

public abstract class AbstractQuestionBankResolver implements QuestionBankResolver {
    @Override
    public QuestionBank resolve(File file, boolean allRight) {

        try {
            // 校验文件合法性
            validateFile(file);
            // 文件类型不一样，解析出有效文字
            String text = resolveValidText(file);
            // 对文字进行分块
            List<String> plainBlocks = resolvePlainBlock(text);
            // 对于每一块，分离出题面和问题
            List<QuestionBlock> questionBlocks = resolveQuestionBlock(plainBlocks);
            checkQuestionBlocksIfNeed(questionBlocks, allRight);
            // 对于每一个组，将问题进行分离
            List<AnswerBlock> answerBlocks = resolveAnswerBlock(questionBlocks);
            checkAnswerBlocksIfNeed(answerBlocks, allRight);
            // 最终我们进行组装
            QuestionBank questionBank = resolveQuestionBank(questionBlocks, answerBlocks);

            return questionBank;

        } catch (Exception e) {
            // 报错说明我们有一步完全失败了，或者在 allRight 为 true 时出现部分错误
            throw new RuntimeException("解析失败", e);
        }
    }

    private void checkAnswerBlocksIfNeed(List<AnswerBlock> answerBlocks, boolean allRight) {
        if (allRight) {
            boolean nonNull = answerBlocks.stream().allMatch(Objects::nonNull);
            if (!nonNull) {
                throw new RuntimeException("解析答案块出现部分异常");
            }
        }
    }

    private void checkQuestionBlocksIfNeed(List<QuestionBlock> questionBlocks, boolean allRight) {
        if (allRight) {
            boolean nonNull = questionBlocks.stream().allMatch(Objects::nonNull);
            if (!nonNull) {
                throw new RuntimeException("解析题目块出现部分异常");
            }
        }
    }

    protected abstract QuestionBank resolveQuestionBank(List<QuestionBlock> questionBlocks, List<AnswerBlock> answerBlocks);

    protected abstract List<AnswerBlock> resolveAnswerBlock(List<QuestionBlock> questionBlocks);

    protected abstract List<QuestionBlock> resolveQuestionBlock(List<String> plainBlocks);

    protected abstract List<String> resolvePlainBlock(String text);

    protected abstract String resolveValidText(File file);

    private void validateFile(File file) {
        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("文件无法被解析");
        }
    }
}
