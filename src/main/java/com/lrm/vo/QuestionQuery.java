package com.lrm.vo;

import com.lrm.po.Tag;

import java.util.List;

public class QuestionQuery
{
    private String title;

    //最后还是要转成tags 容器类有自带的方法可以调用 比较方便
    private List<Tag> tags;
    private String tagIds;

    public QuestionQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTagIds()
    {
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
}
