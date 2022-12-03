package com.moyu.question.bank.evaluate;

import com.moyu.question.bank.evaluate.rule.DelegatingEvaluationRuleResolver;
import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.model.bank.EvaluableQuestion;
import com.moyu.question.bank.model.question.Answer;
import com.moyu.question.bank.model.question.Question;
import com.moyu.question.bank.model.question.QuestionType;
import org.junit.jupiter.api.Test;

class TypeBasedEvaluatorTest {

    @Test
    void testMixed() {
        EvaluationRule rule = DelegatingEvaluationRuleResolver.resolve(
                "mixed|100|" +
                        "4!multipleChoice|60|1:20;1:-4;1:-8;n_" +
                        "1!fillTheBlank|15|n_" +
                        "1!fillTheBlank|15|n_" +
                        "1!trueOrFalse|10|10,-10"
        );
        QuestionType type = new QuestionType();

        EvaluableQuestion evaluableQuestion = new EvaluableQuestion();
        Question question = new Question();

        Answer standardAnswer = Answer.builder()
                .option(1, "选项1", "1")
                .option(2, "选项2", "0")
                .option(3, "选项3", "1")
                .option(4, "选项4", "1")
                .option(5, "填空1", "aabbcc")
                .option(6, "填空2", "ccddee")
                .option(7, "判断", "1")
                .build();

        question.setAnswer(standardAnswer);
        evaluableQuestion.setQuestion(question);
        evaluableQuestion.setEvaluationRule(rule);

        Answer submittedAnswer = Answer.builder()
                .option(1, null, "1")
                .option(2, null, "1")
                .option(3, null, "1")
                .option(4, null, "0")
                .option(5, null, "aabbcc")
                .option(6, null, "ccddee")
                .option(7, null, "1")
                .build();

        GenericEvaluator evaluator = new GenericEvaluator();

        EvaluationResult result = evaluator.evaluate(evaluableQuestion, submittedAnswer);

        System.out.println(result);
    }

}