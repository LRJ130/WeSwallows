package com.lrm.po;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * @author 山水夜止
 */
@Entity
@Table(name = "t_dislikes")
public class DisLikes {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 踩的是评论
     */
    private Boolean dislikeComment;
    /**
     * 踩的是问题
     */
    private Boolean dislikeQuestion;


    /**
     * 多dislikes对一comment
     */
    @ManyToOne
    private Comment comment;

    /**
     * 多dislikes对一question
     */
    @ManyToOne
    private Question question;

    /**
     * 多dislikes对一user
     */
    @ManyToOne
    private User postUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDislikeComment() {
        return dislikeComment;
    }

    public void setDislikeComment(Boolean dislikeComment) {
        this.dislikeComment = dislikeComment;
    }

    public Boolean getDislikeQuestion() {
        return dislikeQuestion;
    }

    public void setDislikeQuestion(Boolean dislikeQuestion) {
        this.dislikeQuestion = dislikeQuestion;
    }

    /**
     * 只需要likes的json对象中含有question或comment 而后二者的json对象中只需要有数量 不需要有dislikes
     * 所以用这种方向的Json控制
     *
     * @return 关联评论
     */
    @JsonManagedReference
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @JsonManagedReference
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @JsonBackReference
    public User getPostUser() {
        return postUser;
    }

    public void setPostUser(User postUser) {
        this.postUser = postUser;
    }

}
