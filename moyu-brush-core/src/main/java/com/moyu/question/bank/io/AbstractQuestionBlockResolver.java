package com.moyu.question.bank.io;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractQuestionBlockResolver extends AbstractPlainBlockResolver {

    private List<ResolveQuestionBlockStrategy> resolveStrategies;
    @Override
    protected List<QuestionBlock> resolveQuestionBlock(List<String> plainBlocks) {
        List<QuestionBlock> questionBlocks = new ArrayList<>();

        for (String plainBlock : plainBlocks) {
            QuestionBlock questionBlock = null;
            for (ResolveQuestionBlockStrategy strategy : resolveStrategies) {
                questionBlock = strategy.resolve(plainBlock);
                if (checkValidQuestionBlock(questionBlock)) {
                    break;
                }
            }
            questionBlocks.add(questionBlock);
        }

        return questionBlocks;
    }

    private boolean checkValidQuestionBlock(QuestionBlock questionBlock) {
        if (questionBlock == null ||
                Strings.isNullOrEmpty(questionBlock.getStem()) ||
                Strings.isNullOrEmpty(questionBlock.getAnswerText())) {

            return false;
        }
        // TODO: 补充逻辑
        return true;
    }
}
