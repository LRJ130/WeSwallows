package com.lrm.po;

import com.lrm.annotation.AccountInfoFormat;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//应用于懒加载 在Repository层要使用@EntityGrahph配置在查询方法上 缺一不可
@NamedEntityGraph(name = "User",
        attributeNodes = {@NamedAttributeNode("postComments"),
                          @NamedAttributeNode("receiveComments")})
@Entity
@Table(name = "t_user")
public class User
{
    @Id
    @GeneratedValue
    private Long id;     //每个类都要有一个id主键
    //用户个人属性
    private Boolean isAdmin;     //是否为管理员
    private Boolean canSpeak;  //能否发言
    //必填部分
        //@NotBlank需要搭配有@Valid的controller方法使用 且只能用在String上
    @NotBlank(message = "请输入昵称")
    private String nickname;
    @AccountInfoFormat(message = "请输入正确账号格式————长度为7至12且不能包含汉字", permit = "true")
    private String username;
    @AccountInfoFormat
    private String password;
    //非必填 可以在前端显示默认值
    private String avatar;
    private String email;
    private String qqId;
    private String wechatId;
        //男true 女false
    private Boolean sex;
    private String personalSignature;
    private String academy;
    private String major;
    //贡献值
    private Integer donation;
    //自动生成时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerTime;


    //关联关系 Merge Refresh Remove Persist
        //mappedBy="name" name为外键所在的表中关联的字段的名字
        //没必要设置Remove 因为不打算做注销账号功能
    @OneToMany(mappedBy = "user")
    private List<Question> questions = new ArrayList<>();
        //懒加载
    @OneToMany(mappedBy = "postUser",fetch = FetchType.LAZY)
    private List<Comment> postComments = new ArrayList<>();
        //懒加载
    @OneToMany(mappedBy = "receiveUser",fetch = FetchType.LAZY)
    private List<Comment> receiveComments = new ArrayList<>();
        //懒加载
    @OneToMany(mappedBy = "postUser",fetch = FetchType.LAZY)
    private List<Likes> postLikes = new ArrayList<>();
        //懒加载
    @OneToMany(mappedBy = "receiveUser",fetch = FetchType.LAZY)
    private List<Comment> receiveLikes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQQId() {
        return qqId;
    }

    public void setQQId(String qqId) {
        this.qqId = qqId;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getDonation() {
        return donation;
    }

    public void setDonation(Integer donation) {
        this.donation = donation;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Boolean getCanSpeak() {
        return canSpeak;
    }

    public void setCanSpeak(Boolean canSpeak) {
        this.canSpeak = canSpeak;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Comment> getPostComments() {
        return postComments;
    }

    public void setPostComments(List<Comment> postComments) {
        this.postComments = postComments;
    }

    public List<Comment> getReceiveComments() {
        return receiveComments;
    }

    public void setReceiveComments(List<Comment> receiveComments) {
        this.receiveComments = receiveComments;
    }

    public List<Likes> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(List<Likes> postLikes) {
        this.postLikes = postLikes;
    }

    public List<Comment> getReceiveLikes() {
        return receiveLikes;
    }

    public void setReceiveLikes(List<Comment> receiveLikes) {
        this.receiveLikes = receiveLikes;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", isAdmin=" + isAdmin +
                ", canSpeak=" + canSpeak +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", qqId='" + qqId + '\'' +
                ", wechatId='" + wechatId + '\'' +
                ", sex=" + sex +
                ", personalSignature='" + personalSignature + '\'' +
                ", academy='" + academy + '\'' +
                ", major='" + major + '\'' +
                ", donation=" + donation +
                ", registerTime=" + registerTime +
                ", questions=" + questions +
                ", postComments=" + postComments +
                ", receiveComments=" + receiveComments +
                ", postLikes=" + postLikes +
                ", receiveLikes=" + receiveLikes +
                '}';
    }
}

