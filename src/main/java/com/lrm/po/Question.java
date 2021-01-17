package com.lrm.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_question")
public class Question
{
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @NotBlank(message = "请输入内容")
    private String content;
    @Lob
    @NotBlank(message = "请输入概述")
    private String description;
    @NotBlank(message = "请输入标题")
    private String title;

    private Integer view;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //无级联关系
    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Likes> likes;

    //允许级联删除 删除问题即删除所有评论
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public void setLikes(List<Likes> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", view=" + view +
                ", createTime=" + createTime +
                ", tags=" + tags +
                ", user=" + user +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}
