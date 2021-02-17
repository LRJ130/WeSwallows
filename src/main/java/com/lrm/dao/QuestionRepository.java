package com.lrm.dao;

import com.lrm.po.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question,Long>, JpaSpecificationExecutor<Question> {


    @Query("select q from Question q where q.title like ?1 or q.content like ?1")
    Page<Question> findByQuery(String query, Pageable pageable);


//    查找自己发布过的问题
//    @Query("select function('date_format', q.updateTime, '%Y') as year from Question b group by function('date_format', q.updateTime, '%Y') order by year desc ")
//    List<String> findGroupYear();
//    @Query("select q from Question  q where function('date_format', q.updateTime, '%Y') = ?1")
//    List<Question> findByYear(String year);



}
