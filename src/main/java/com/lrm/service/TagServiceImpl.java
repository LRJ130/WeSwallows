package com.lrm.service;

import com.lrm.dao.TagRepository;
import com.lrm.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;

    //简单的增删改查
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.delete(id);
    }

    @Override
    public Tag updateTag(Tag tag) {
        Tag t = tagRepository.findOne(tag.getId());
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }

    //通过名字找标签
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    //标签分页
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    //所有标签展示
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    //部分标签展示
    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "questions.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return tagRepository.findTop(pageable);
    }

    //将String对象转为Tag集合
    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        return tagRepository.findAll(convertToList(ids));
    }
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
