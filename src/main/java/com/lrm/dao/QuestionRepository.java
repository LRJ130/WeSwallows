package com.lrm.dao;

import com.lrm.po.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long>, JpaSpecificationExecutor<Question> {


    @Query("select q from Question q where q.title like ?1 or q.content like ?1")
    Page<Question> findByQuery(String query, Pageable pageable);

    @Query("select function('date_format', q.newCommentedTime, '%Y') as year from Question q where q.id = ?1 group by function('date_format', q.newCommentedTime, '%Y') order by year desc ")
    List<String> findGroupYear(Long userId);

    @Query("select q from Question  q where function('date_format', q.newCommentedTime, '%Y') = ?1 order by q desc")
    List<Question> findByYear(String year);

    //一个用户提出了多少个问题
    Long countAllByUserId(Long userId);

}
