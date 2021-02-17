package com.lrm.dao;

import com.lrm.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    //得到所有第一级评论
    List<Comment> findByQuestionIdAndParentCommentNull(Long questionId, Sort sort);
}
