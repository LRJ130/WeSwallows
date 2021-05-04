package com.lrm.service;

import com.lrm.dao.DisLikesRepository;
import com.lrm.po.Comment;
import com.lrm.po.DisLikes;
import com.lrm.po.Question;
import com.lrm.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author 山水夜止
 */
@Service
public class DisLikesServiceImpl implements DisLikesService {
    @Autowired
    DisLikesRepository disLikesRepository;

    @Transactional
    @Override
    public DisLikes saveDisLikes(DisLikes disLikes, User postUser) {
        disLikes.setPostUser(postUser);
        return disLikesRepository.save(disLikes);
    }

    @Override
    @Transactional
    public DisLikes saveDisLikes(DisLikes disLikes) {
        return disLikesRepository.save(disLikes);
    }

    @Override
    @Transactional
    public void deleteDisLikes(DisLikes disLikes) {
        disLikesRepository.delete(disLikes);
    }

    @Override
    public DisLikes getDisLikes(User postUser, Question question) {
        return disLikesRepository.findByPostUserAndQuestion(postUser, question);
    }

    @Override
    public DisLikes getDisLikes(User postUser, Comment comment) {
        return disLikesRepository.findByPostUserAndComment(postUser, comment);
    }

    @Override
    public DisLikes getDisLikes(Long disLikesId) {
        Optional<DisLikes> disLikes = disLikesRepository.findById(disLikesId);
        return disLikes.orElse(null);
    }

}
