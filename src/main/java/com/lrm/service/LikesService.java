package com.lrm.service;

import com.lrm.po.Comment;
import com.lrm.po.Likes;
import com.lrm.po.Question;
import com.lrm.po.User;

import java.util.List;

public interface LikesService {
    Likes saveLikes(Likes likes, User postUser, User receiveUser);
    void deleteLikes(Likes likes);
    Likes getLikes(User postUser, Question question);
    Likes getLikes(User postUser, Comment comment);
    Likes getLikes(Long LikesId);

    List<Likes> listAllNotReadComment(Long userId);


}
