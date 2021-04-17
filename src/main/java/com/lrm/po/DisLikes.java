package com.lrm.po;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_dislikes")
public class DisLikes {
    @Id
    @GeneratedValue
    private Long id;

    Boolean dislikeComment;
    Boolean dislikeQuestion;

//    是否已读
//    Boolean isRead;

//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createTime;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private Question question;

    @ManyToOne
    private User postUser;
//    @ManyToOne
//    private User receiveUser;

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

//    public Boolean getRead() {
//        return isRead;
//    }
//
//    public void setRead(Boolean read) {
//        isRead = read;
//    }
//
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }

    //因为我想让likes的json对象中含有question或comment 而后二者的json对象中只需要有数量 不需要有对象
    // 所以用这种方向的Json控制
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

//    public User getReceiveUser() {
//        return receiveUser;
//    }
//
//    public void setReceiveUser(User receiveUser) {
//        this.receiveUser = receiveUser;
//    }
//
//    @Override
//    public String toString() {
//        return "Likes{" +
//                "likeComment=" + likeComment +
//                ", likeQuestion=" + likeQuestion +
//                ", isRead=" + isRead +
//                ", id=" + id +
//                ", createTime=" + createTime +
//                ", comment=" + comment +
//                ", question=" + question +
//                ", postUser=" + postUser +
//                ", receiveUser=" + receiveUser +
//                '}';
//    }
}
