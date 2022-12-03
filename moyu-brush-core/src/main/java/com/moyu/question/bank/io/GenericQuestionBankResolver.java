package com.moyu.question.bank.io;

import com.moyu.question.bank.model.bank.QuestionBank;

import java.util.List;

public class GenericQuestionBankResolver extends AbstractAnswerBlockResolver {

    @Override
    protected QuestionBank resolveQuestionBank(List<QuestionBlock> questionBlocks, List<AnswerBlock> answerBlocks) {
        // TODO
        QuestionBank questionBank = QuestionBank.builder()
                .metadata(null)
                .name(null)
                .evaluableQuestions(null)
                .build();
        return questionBank;
    }
}
