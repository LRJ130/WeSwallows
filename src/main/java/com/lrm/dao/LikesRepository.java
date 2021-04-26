package com.lrm.dao;

import com.lrm.po.Comment;
import com.lrm.po.Likes;
import com.lrm.po.Question;
import com.lrm.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Long> {

    Likes findByPostUserAndQuestion(User postUser, Question question);
    Likes findByPostUserAndComment(User postUser, Comment comment);

    List<Likes> findByReceiveUserIdAndLooked(Long userId, Boolean isLooked);
}
