package com.lrm.service;

import com.lrm.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByQuestionId(Long questionId);

    Comment saveComment(Comment comment);
}
