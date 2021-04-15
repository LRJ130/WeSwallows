package com.lrm.po;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//注解大多写在了User类

@Entity
@Table(name = "t_tag")
public class Tag
{
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "请输入标签名称")
    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "parentTag")
    private List<Tag> sonTags = new ArrayList<>();
    @JsonBackReference
    @ManyToOne
    private Tag parentTag;
    //避免json序列无限递归 只好出此下策 真拙劣啊...
    Long parentTagId0;

    //不用级联删除 这块需要返回错误页面 告知管理员标签下有博客的情况下不能删除标签
    @JsonBackReference
    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonBackReference
    public List<Question> getQuestions() {
        return questions;
    }
    @JsonBackReference
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @JsonManagedReference
    public List<Tag> getSonTags() {
        return sonTags;
    }
    @JsonManagedReference
    public void setSonTags(List<Tag> sonTags) {
        this.sonTags = sonTags;
    }

    @JsonBackReference
    public Tag getParentTag() {
        return parentTag;
    }
    @JsonBackReference
    public void setParentTag(Tag parentTag) {
        this.parentTag = parentTag;
    }

    public Long getParentTagId0() {
        return parentTagId0;
    }

    public void setParentTagId0(Long parentTagId0) {
        this.parentTagId0 = parentTagId0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) o;
        return getId().equals(tag.getId()) && getName().equals(tag.getName()) && getSonTags().equals(tag.getSonTags()) && getParentTag().equals(tag.getParentTag()) && getQuestions().equals(tag.getQuestions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSonTags(), getQuestions());
    }

//    会无限迭代 先不写toString了
//    @Override
//    public String toString() {
//        return "Tag{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", sonTags=" + sonTags +
//                ", parentTag=" + parentTag +
//                ", questions=" + questions +
//                '}';
//    }
}
