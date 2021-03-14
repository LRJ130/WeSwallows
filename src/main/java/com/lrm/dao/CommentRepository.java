package com.lrm.dao;

import com.lrm.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 山水夜止
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

    /**
     * 查询问题下的第一级评论
     * @param questionId 问题id
     * @param sort 排序顺序
     * @param answer 哪类回答
     * @return 评论集合
     */
    List<Comment> findByQuestionIdAndParentCommentNullAndAnswer(Long questionId, Sort sort, Boolean answer);


    /**
     * 得到问题对应的所有评论
     * @param questionId 问题对应id
     * @return 评论集合
     */
    List<Comment> findByQuestionId(Long questionId);


    /**
     * 查询用户未读评论
     * @param userId 用户Id
     * @param isRead 是否已读
     * @return 评论集合
     */
    List<Comment> findByReceiveUserIdAndIsRead(Long userId, Boolean isRead);
}
