package com.lrm.dao;

import com.lrm.po.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 山水夜止
 */
public interface QuestionRepository extends JpaRepository<Question,Long>, JpaSpecificationExecutor<Question> {


    /**
     * 查询标题或正文含有query的问题
     * @param query 查询条件
     * @param pageable 分页对象
     * @return 查询结果
     */
    @Query("select q from Question q where q.title like ?1 or q.content like ?1")
    Page<Question> findByQuery(String query, Pageable pageable);


    /**
     * 用户发布的问题中 分别有哪年的
     * @param userId 用户id
     * @return  时间（年）集合
     */
    @Query("select function('date_format', q.newCommentedTime, '%Y') as year from Question q where q.user.id = ?1 group by function('date_format', q.newCommentedTime, '%Y') order by year desc ")
    List<String> findGroupYear(Long userId);

    /**
     * 按年查询用户发布的问题 并按时间分类
     * @param year 时间
     * @param userId 用户id
     * @return 在这个时间段发布的问题
     */
    @Query("select q from Question  q where function('date_format', q.newCommentedTime, '%Y') = ?1 and q.user.id = ?2 order by q desc")
    List<Question> findByYear(String year, Long userId);

    /**
     * 一个用户提出了多少个问题
     * @param userId 用户Id
     * @return 多少个
     */
    Long countAllByUserId(Long userId);

}
