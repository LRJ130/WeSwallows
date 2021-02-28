package com.lrm.po;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_likes")
public class Likes {
    @Id
    @GeneratedValue
    private Long id;

    //标识是bd问题还是支持评论
    Boolean likeComment;
    Boolean likeQuestion;
    Boolean isRead;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToOne
    private Comment comment;
    @ManyToOne
    private Question question;

    @ManyToOne
    private User postUser;
    @ManyToOne
    private User receiveUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLikeComment() {
        return likeComment;
    }

    public void setLikeComment(Boolean likeComment) {
        this.likeComment = likeComment;
    }

    public Boolean getLikeQuestion() {
        return likeQuestion;
    }

    public void setLikeQuestion(Boolean likeQuestion) {
        this.likeQuestion = likeQuestion;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getPostUser() {
        return postUser;
    }

    public void setPostUser(User postUser) {
        this.postUser = postUser;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    @Override
    public String toString() {
        return "Likes{" +
                "likeComment=" + likeComment +
                ", likeQuestion=" + likeQuestion +
                ", isRead=" + isRead +
                ", id=" + id +
                ", createTime=" + createTime +
                ", comment=" + comment +
                ", question=" + question +
                ", postUser=" + postUser +
                ", receiveUser=" + receiveUser +
                '}';
    }
}
