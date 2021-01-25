package com.lrm.service;

import com.lrm.NotFoundException;
import com.lrm.dao.QuestionRepository;
import com.lrm.po.Question;
import com.lrm.util.MyBeanUtils;
import com.lrm.vo.QuestionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class QuestionServiceImpl implements QuestionService{
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Page<Question> listQuestion(Pageable pageable, QuestionQuery Question) {
        return null;
    }

    @Override
    public Page<Question> listQuestion(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Question> listQuestion(Long tagId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Question> listRecommendQuestionTop(Integer size) {
        return null;
    }

    @Override
    public Question saveQuestion(Question Question) {
        return null;
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.delete(id);
    }

    @Override
    public Question updateQuestion(Long id, Question question) {
        Question q = questionRepository.findOne(id);
        if(q == null)
        {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(question, q, MyBeanUtils.getNullPropertyNames(question));
        return questionRepository.save(q);
    }

    @Override
    public Question getQuestion(Long id) {
        return questionRepository.findOne(id);
    }

    @Override
    public Long countQuestion() {
        return null;
    }
}
