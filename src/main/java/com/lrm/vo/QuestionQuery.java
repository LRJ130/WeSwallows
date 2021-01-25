package com.lrm.vo;

import java.util.List;

//通过typeId查询文章
public class QuestionQuery
{
    private String title;
    private List<Long> tagIds;
    public QuestionQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
