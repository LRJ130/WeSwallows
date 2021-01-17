package com.lrm.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//注解大多写在了User类

@NamedEntityGraph(name = "Tag",
        attributeNodes = {@NamedAttributeNode("questions")})
@Entity
@Table(name = "t_tag")
public class Tag
{
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "请输入标签名称")
    private String name;

    //不用级联删除 这块需要返回错误页面 告知管理员标签下有博客的情况下不能删除标签
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", questions=" + questions +
                '}';
    }
}
