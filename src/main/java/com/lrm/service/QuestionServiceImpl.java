package com.lrm.service;

import com.lrm.dao.QuestionRepository;
import com.lrm.po.Question;
import com.lrm.vo.MyBeanUtils;
import com.lrm.vo.QuestionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

public class QuestionServiceImpl implements QuestionService{
    @Autowired
    private QuestionRepository questionRepository;

    //一个数据库事务由一条或者多条sql语句构成，它们形成一个逻辑的工作单元。这些sql语句要么全部执行成功，要么全部执行失败 。
    @Transactional
    @Override
    public Question saveQuestion(Question question) {
        //时间是date对象所以新增的时候需要初始化 否则为null;
        question.setCreateTime(new Date());
        question.setNewCommentedTime(new Date());
        question.setView(0);
        //初始化点赞数为0
        question.setLikesNum(0);
        //根据发布问题人的贡献初始化问题的影响力
        question.setImpact(question.getUser().getDonation()*4);
        return questionRepository.save(question);
    }

    @Transactional
    @Override
    public Question updateQuestionTime(Question question) {
        question.setNewCommentedTime(new Date());
        return questionRepository.save(question);
    }

    public Question updateQuestion(Question question)
    {
        Question q = questionRepository.findOne(question.getId());
        BeanUtils.copyProperties(question, q, MyBeanUtils.getNullPropertyNames(question));
        return questionRepository.save(q);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.delete(id);
    }

    @Override
    public Question getQuestion(Long id) {
        return questionRepository.findOne(id);
    }

    @Override
    public Long countQuestion() {
        return questionRepository.count();
    }


    @Override
    public Page<Question> listQuestion(Pageable pageable, QuestionQuery question) {
        return questionRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //""是空字节，而不是null 下面这种写法 而不是Question.getTitle().equals("")是防止它是null 从而造成空指针异常
            if( question.getTitle() !=null && !"".equals(question.getTitle()) )
            {
                predicates.add(cb.like(root.get("title"), "%"+question.getTitle()+"%"));
            }
            if( question.getTagIds() !=null)
            {
                predicates.add(cb.like(root.get("tagIds"), "%"+question.getTagIds()+"%"));
            }
            cq.where(predicates.toArray(new Predicate[0]));
            return null;
        }, pageable);
    }

    @Override
    public Page<Question> listQuestionPlusUserId(Pageable pageable, QuestionQuery question, Long userId) {
        return questionRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //""是空字节，而不是null 下面这种写法 而不是Question.getTitle().equals("")是防止它是null 从而造成空指针异常
            if( question.getTitle() !=null && !"".equals(question.getTitle()) )
            {
                predicates.add(cb.like(root.get("title"), "%"+question.getTitle()+"%"));
            }
            if( question.getTagIds() !=null)
            {
                //这里应该怎么写呢
                predicates.add(cb.like(root.get("tagIds"), "%"+question.getTagIds()+"%"));
            }
            Join join = root.join("user");
            predicates.add(cb.equal(join.get("id"), userId));

            cq.where(predicates.toArray(new Predicate[0]));
            return null;
        }, pageable);
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


}
