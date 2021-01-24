package com.lrm.service;

import com.lrm.po.Question;
import com.lrm.vo.QuestionQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    //简单的增删改查
    Question saveQuestion(Question Question);
    void deleteQuestion(Long id);
    Question updateQuestion(Long id, Question Question);
    Question getQuestion(Long id);
    Long countQuestion();

    //返回搜索后Question
    Page<Question> listQuestion(Pageable pageable, QuestionQuery Question);

    //返回首页Question
    Page<Question> listQuestion(Pageable pageable);

    //返回标签所属Question
    Page<Question> listQuestion(Long tagId, Pageable pageable);

    //markdown转换
    //Question getAndConvert(Long id);

    //返回推荐值最高的size个Question
    List<Question> listRecommendQuestionTop(Integer size);

    //按日期顺序返回自己的问题
    //Map<String, List<Question>> archivesQuestion();

}
