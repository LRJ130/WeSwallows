package com.lrm.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_comment")
public class Comment
{
    @Id
    @GeneratedValue
    private Long id;
    //如果标识为1 即question.user是发布这个comment的人 那么它应该有一个标识
    private Boolean adminComment;
    private Boolean isRead;

    @Lob
    @NotBlank
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToOne
    private Question question;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>();
    @ManyToOne
    private Comment parentComment;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Likes> likes;

    @ManyToOne
    private User receiveUser;
    @ManyToOne
    private User postUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(Boolean adminComment) {
        this.adminComment = adminComment;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Comment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public User getPostUser() {
        return postUser;
    }

    public void setPostUser(User postUser) {
        this.postUser = postUser;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public void setLikes(List<Likes> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", adminComment=" + adminComment +
                ", isRead=" + isRead +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", question=" + question +
                ", replyComments=" + replyComments +
                ", parentComment=" + parentComment +
                ", likes=" + likes +
                ", receiveUser=" + receiveUser +
                ", postUser=" + postUser +
                '}';
    }
}
