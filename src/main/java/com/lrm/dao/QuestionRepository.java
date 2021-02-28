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

    //一个用户提出了多少个问题
    //Integer countAllByUser(User user);




}
