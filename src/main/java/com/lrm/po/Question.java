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

    //懒加载 只有getContent了才加载
    @Basic(fetch = FetchType.LAZY)
    @Lob
    @NotBlank(message = "请输入内容")
    private String content;
    @Lob
    @NotBlank(message = "请输入概述")
    private String description;
    @NotBlank(message = "请输入标题")
    private String title;
    //浏览次数
    private Integer view;
    //获得点赞数量
    private Integer likesNum;
    //被评论数
    private Integer commentsNum;
    //点踩
    private Integer disLikesNum;
    //是否被隐藏
    private Boolean isHidden;

    //占比待定
    //问题的影响力 推荐 impact=user.donation*4+question.view*2+question.comment.count*2+question.comment.maxLikes*2
    //user.donation=user.comment2.count*3++user.comment2.likes*3+user.question.count*2+user.question.likes*2
    private Integer impact;
    //显式的点赞数
    //private Integer likesNum;

    //不通过数据库与前端交互 直接在service层转化为tags
    @Transient
    private  String tagIds;

    //被它修饰的时间会封装成完整的"yyyy-MM-dd HH:mm:ss"的Date类型
      //首页展示根据发布时间展示
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
      //推荐时根据最新被评论时间展示
    @Temporal(TemporalType.TIMESTAMP)
    private Date newCommentedTime;

    //无级联关系
    @ManyToMany
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
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

    public Date getCreateTime() {
        return createTime;
    }

    public Integer getImpact() {
        return impact;
    }

    public void setImpact(Integer impact) {
        this.impact = impact;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getNewCommentedTime() {
        return newCommentedTime;
    }

    public void setNewCommentedTime(Date newCommentedTime) {
        this.newCommentedTime = newCommentedTime;
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

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    //真正起setTagIds作用的是这个方法
    //Tag集合转为String对象
    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    //前端Tag对象的格式是以,分割的 tagIds作为一个媒介
    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", view=" + view +
                ", likesNum=" + likesNum +
                ", commentsNum=" + commentsNum +
                ", disLikesNum=" + disLikesNum +
                ", isHidden=" + isHidden +
                ", impact=" + impact +
                ", tagIds='" + tagIds + '\'' +
                ", createTime=" + createTime +
                ", newCommentedTime=" + newCommentedTime +
                ", tags=" + tags +
                ", user=" + user +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}
