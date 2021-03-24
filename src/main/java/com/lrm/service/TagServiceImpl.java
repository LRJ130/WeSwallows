package com.lrm.service;

import com.lrm.dao.TagRepository;
import com.lrm.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 山水夜止
 */
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;

    /**
     * 临时存放标签
     */
    public Set<Tag> tagSet = new HashSet<>();

    //简单的增删改查

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        Long parentTagId = tag.getParentTag().getId();
        if (parentTagId != -1) {
            tag.setParentTag(tagRepository.findOne(parentTagId));
        } else {
            //对象new了(初始化id为-1了) 但没有持久化会报错 所以设成null
            tag.setParentTag(null);
        }
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.delete(id);
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) {
        Tag t = tagRepository.findOne(tag.getId());
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }

    /**
     * 通过用户名找标签 查询是否重复
     */
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    /**
     * 首级标签展示
     */
    @Override
    public List<Tag> listTagTop() {
        return tagRepository.findByParentTagNull();
    }

    /**
     * 将String对象转为Tag集合
     * @param ids 前端以,分割的tagId
     * @return 标签集合
     */
    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        return tagRepository.findAll(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idArray = ids.split(",");
            for (String s : idArray) {
                list.add(new Long(s));
            }
        }
        return list;
    }

    /**
     * 由某标签列出其下所有标签
     */
    @Override
    public Set<Tag> listTags (Tag tag)
    {
        tagSet.add(tag);
        List<Tag> tags = tag.getSonTags();
        if (tags.size() == 0)
        {
            return tagSet;
        }
        for (Tag tag1 : tags)
        {
            listTags(tag1);

        }
        return tagSet;
    }

}
