package com.lrm.service;

import com.lrm.dao.CommentRepository;
import com.lrm.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private QuestionService questionService;

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();



    //得到问题下所有评论
    @Override
    public List<Comment> listCommentByQuestionId(Long questionId) {
        Sort sort = new Sort(Sort.Direction.ASC,"createTime");
        //得到所有第一级评论
        List<Comment> comments = commentRepository.findByQuestionIdAndParentCommentNull(questionId, sort);
        return eachComment(comments);
    }

    @Override
    public Comment getComment(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    //遍历所有第一级评论
    private List<Comment> eachComment(List<Comment> comments) {
        //将所有第一级评论保存到commentsView里
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments) {
        //遍历所有第一级评论
        for (Comment comment : comments) {
            //得到回复第一级评论的第二级评论
            List<Comment> replys1 = comment.getReplyComments();
            //遍历第二级评论
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    private void recursively(Comment comment) {
        tempReplys.add(comment);//第二级评论添加到临时存放集合
        //如果第二级评论有子评论
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            //遍历第三级评论 添加到临时存放集合
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }

    //保存评论 如果不是通过回复的方式 那么前端传回parentCommentId默认设置为1
    @Transactional
    @Override
    public Comment saveComment(Comment comment, Long questionId) {
        Long parentCommentId = comment.getParentComment().getId();
        //有父评论 则获得通知的人是父评论的发出者 否则为question的发出者
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findOne(parentCommentId));
            comment.setQuestion(questionService.getQuestion(questionId));
            comment.setReceiveUser(getComment(parentCommentId).getPostUser());
        } else {
            //在第一行comment.getParentComment中实际上new了一个parentComment对象(初始化id为-1了) 但id不能为-1 没有将p...持久化所以会报错 要设成null
            comment.setParentComment(null);
            comment.setQuestion(questionService.getQuestion(questionId));
            comment.setReceiveUser(questionService.getQuestion(questionId).getUser());
        }
        comment.setRead(false);
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

}
