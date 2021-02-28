package com.lrm.dao;

import com.lrm.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    //得到所有第一级的评论
    List<Comment> findByQuestionIdAndParentCommentNullAndAnswer(Long questionId, Sort sort, Boolean answer);


    //一个用户有多少个有效评论
    //Integer countAllByPostUserAndIsAnswer(User postUser, Boolean isAnswer);

    //一个问题下有多少评论
    //Integer countAllByQuestion(Question question);

    //得到所有评论
    List<Comment> findByQuestionId(Long questionId);

    List<Comment> findByReceiveUserIdAndIsRead(Long userId, Boolean isRead);
}
