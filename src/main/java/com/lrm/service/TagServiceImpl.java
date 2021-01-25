package com.lrm.service;

import com.lrm.dao.TagRepository;
import com.lrm.po.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        return null;
    }

    @Override
    public List<Tag> listTag(String ids) {
        return null;
    }
}
