package com.lrm.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class DividePage {

    public static <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

}
