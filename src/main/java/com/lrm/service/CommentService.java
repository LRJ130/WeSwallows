package com.lrm.service;

import com.lrm.po.Comment;

import java.util.List;

public interface CommentService {

    Comment saveComment(Comment comment, Long questionId);

    List<Comment> listCommentByQuestionId(Long questionId);

    Comment getComment(Long commentId);
}
