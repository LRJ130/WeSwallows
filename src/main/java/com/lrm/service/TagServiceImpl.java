package com.lrm.service;

import com.lrm.dao.TagRepository;
import com.lrm.po.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;


    @Override
    public Tag updateTag(Long id, Tag type) {
        return null;
    }

    @Override
    public void deleteTag(Long id) {

    }

    @Override
    public Tag saveTag(Tag type) {
        return null;
    }

    @Override
    public Tag getTag(Long id) {
        return null;
    }

    @Override
    public Tag getTagByName(String name) {
        return null;
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return null;
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        return null;
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
