package com.lrm.service;

import com.lrm.po.Question;

public interface QuestionService {
    //简单的增删改查
    Question getQuestion(Long id);
    void deleteQuestion(Long id);
}
