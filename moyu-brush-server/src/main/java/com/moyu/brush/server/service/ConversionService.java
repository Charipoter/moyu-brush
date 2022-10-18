package com.moyu.brush.server.service;

import com.moyu.brush.server.model.po.QuestionBankPO;
import com.moyu.brush.server.model.po.QuestionPO;
import com.moyu.question.bank.model.bank.QuestionBank;
import com.moyu.question.bank.model.question.Question;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ConversionService {

    Question questionPO2Question(QuestionPO questionPO);

    QuestionPO question2QuestionPO(Question question);

    List<Question> questionPOList2QuestionList(List<QuestionPO> questionPOList) throws ExecutionException, InterruptedException;

    QuestionBank questionBankPO2QuestionBank(QuestionBankPO questionBankPO);

}
