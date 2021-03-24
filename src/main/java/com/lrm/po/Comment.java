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
    //通知是否已读
    private Boolean isRead;
    //是否是第二类回答
    private Boolean isAnswer;
    //点赞数
    private Integer likesNum;
    //被评论数
    private Integer commentsNum;
    //点踩数
    private Integer disLikesNum;
    //是否被隐藏 后端只做属性设置的处理 仍然返回全部数据 前端做hidden判断
    private Boolean isHidden;


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

    //消息的接收者
    @ManyToOne
    private User receiveUser;
    //消息的发送者
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

    public Boolean getAnswer() {
        return isAnswer;
    }

    public void setAnswer(Boolean answer) {
        isAnswer = answer;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public Integer getDisLikesNum() {
        return disLikesNum;
    }

    public void setDisLikesNum(Integer disLikesNum) {
        this.disLikesNum = disLikesNum;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public Integer getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Integer likesNum) {
        this.likesNum = likesNum;
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
                ", isAnswer=" + isAnswer +
                ", likesNum=" + likesNum +
                ", commentsNum=" + commentsNum +
                ", disLikesNum=" + disLikesNum +
                ", isHidden=" + isHidden +
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
