package com.moyu.question.bank.io;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnswerBlockResolver extends AbstractQuestionBlockResolver {

    private List<ResolveAnswerBlockStrategy> resolveStrategies;
    @Override
    protected List<AnswerBlock> resolveAnswerBlock(List<QuestionBlock> questionBlocks) {
        List<AnswerBlock> answerBlocks = new ArrayList<>();

        for (QuestionBlock questionBlock : questionBlocks) {
            AnswerBlock answerBlock = null;
            if (questionBlock != null) {

                for (ResolveAnswerBlockStrategy strategy : resolveStrategies) {
                    answerBlock = strategy.resolve(questionBlock.getAnswerText());
                    if (checkValidAnswerBlock(answerBlock)) {
                        break;
                    }
                }
            }
            answerBlocks.add(answerBlock);
        }

        return answerBlocks;
    }

    private boolean checkValidAnswerBlock(AnswerBlock answerBlock) {
        List<String> answers = answerBlock.getAnswers();
        if (answers == null || answers.size() == 0) {
            return false;
        }
        if (answers.stream().anyMatch(Strings::isNullOrEmpty)) {
            return false;
        }
        // TODO: 补充逻辑
        return true;
    }
}
