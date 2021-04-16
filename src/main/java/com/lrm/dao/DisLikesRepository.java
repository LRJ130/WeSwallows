package com.lrm.dao;

import com.lrm.po.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisLikesRepository extends JpaRepository<DisLikes, Long> {
    DisLikes findByPostUserAndQuestion(User postUser, Question question);

    DisLikes findByPostUserAndComment(User postUser, Comment comment);
}
