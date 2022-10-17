package com.moyu.question.bank.evaluate.rule;


import com.moyu.question.bank.evaluate.EvaluationResult;
import com.moyu.question.bank.model.question.AnswerOption;

import java.util.List;

/**
 * 每一道题都可以对应一个规则，用于判分
 * 规则初始规范：
 * (                 full                                       )
 * (section-id)|(section-score)|(          section-rule         )
 *                              ( item );( item );(    item     )
 *                                                (part),( part )
 *                                                       ( n: m )
 *
 * 对于混合模式：
 * (                        full                                )
 * (section-id)|(section-score)|(          section-rule         )
 *                              (n);( full rule)-(n);( full rule)
 *
 * section 级别可以缺省
 */
public interface EvaluationRule {

    EvaluationResult apply(List<AnswerOption> standardAnswerOptions, List<AnswerOption> submittedAnswerOptions);

}
