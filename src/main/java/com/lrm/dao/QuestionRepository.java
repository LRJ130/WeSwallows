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
     * 将所有问题按时间的年分割
     * @param userId 当前用户Id
     * @return  顺序返回年份List集合
     */
    @Query("select function('date_format', q.createTime, '%Y') as year " +
            "from Question q where q.user.id = ?1 order by year desc ")
    List<String> findGroupYear(Long userId);


    /**
     * 查询该年份下发布过问题的所有月份
     * @param year 需要查询的年份
     * @param userId 当前用户Id
     * @return 月份的List集合
     */
    @Query("select function('date_format', q.createTime, '%M') as month " +
            "from Question q where function('date_format', q.createTime, '%Y') = ?1 and q.user.id = ?2" +
            " order by month desc ")
    List<String> findGroupMonthByYear(String year, Long userId);


    /**
     * 按月份查询问题
     * @param year 年份
     * @param month 月份
     * @param userId 当前用户Id
     * @return 问题的List集合
     */
    @Query("select q from Question q where function('date_format', q.createTime, '%Y') = ?1 and " +
            "function('date_format', q.createTime, '%M') = ?2 and q.user.id = ?3 order by q desc")
    List<Question> findByYearAndMonth(String year, String month, Long userId);

    /**
     * 一个用户提出了多少个问题
     * @param userId 用户Id
     * @return 多少个
     */
    Long countAllByUserId(Long userId);

}
