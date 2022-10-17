package com.moyu.question.bank.model.bank;

import com.moyu.question.bank.evaluate.rule.EvaluationRule;
import com.moyu.question.bank.model.question.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankItem {

    private Question question;
    private EvaluationRule evaluationRule;

}
