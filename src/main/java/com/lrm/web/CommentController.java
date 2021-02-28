package com.lrm.web;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lrm.po.Comment;
import com.lrm.po.Likes;
import com.lrm.po.Question;
import com.lrm.po.User;
import com.lrm.service.CommentService;
import com.lrm.service.LikesService;
import com.lrm.service.QuestionService;
import com.lrm.service.UserService;
import com.lrm.util.JWTUtils;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/{questionId}")
public class CommentController
{
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private LikesService likesService;

    //ajax
    @GetMapping("/comments")
    public Result<Map<String, Object>> comments(@PathVariable Long questionId)
    {
        Map<String,Object> hashMap = new HashMap<>();
        //分别返回两类评论
        hashMap.put("comments1", commentService.listCommentByQuestionId(questionId, false));
        hashMap.put("comments2", commentService.listCommentByQuestionId(questionId, true));
        return new Result<>(hashMap, true, "");
    }

    //提交表单后 到这里 然后得到id 然后刷新评论
    @PostMapping("/comments")
    public Result<Map<String, Object>> post(Comment comment, HttpServletRequest request) throws JWTVerificationException
    {
        Map<String, Object> hashMap= new HashMap<>();
        String token = request.getHeader("token");
        DecodedJWT decodedJWT = JWTUtils.getToken(token);
        Long userId = decodedJWT.getClaim("userId").asLong();
        User postUser = userService.getUser(userId);
        Long questionId = comment.getQuestion().getId();

        commentService.saveComment(comment, questionId, postUser);
        questionService.getQuestion(questionId).setNewCommentedTime(new Date());
        if(commentService.getComment(comment.getId()) != null)
        {
            hashMap.put("comments",comment);
            return new Result<>(hashMap, true,"发布成功");
        } else
        {
            return new Result<>(null, false, "发布失败");
        }
    }

    //点赞
    @GetMapping("/comment/{commentId}/approve")
    public void approve(@PathVariable Long questionId, @PathVariable Long commentId, HttpServletRequest request) throws JWTVerificationException
    {
        Comment comment = commentService.getComment(commentId);
        if(comment.getAnswer()) {
            String token = request.getHeader("token");
            DecodedJWT decodedJWT = JWTUtils.getToken(token);
            Long postUserId = decodedJWT.getClaim("userId").asLong();
            User postUser = userService.getUser(postUserId);
            User receiveUser = comment.getReceiveUser();
            Likes likes = likesService.getLikes(postUser, comment);
            if (likes != null) {
                likesService.deleteLikes(likes);
            } else {
                Likes likes1 = new Likes();
                likes1.setLikeQuestion(false);
                likes1.setLikeComment(true);
                comment.setLikesNum(comment.getLikesNum() + 1);
                //点赞前的最高赞数
                Integer maxNum0 = getMaxLikesNum(commentService.listAllCommentByQuestionId(questionId));
                likesService.saveLikes(likes1, postUser, receiveUser);
                //问题被点赞 提问者贡献值+3
                receiveUser.setDonation(receiveUser.getDonation() + 3);
                //提问者贡献值对问题影响力+12
                    //点赞后的最高赞数
                Integer maxNum1 = getMaxLikesNum(commentService.listAllCommentByQuestionId(questionId));
                Question question = questionService.getQuestion(questionId);
                question.setImpact(question.getImpact() + 2*(maxNum1-maxNum0) + 12);
            }
        }
    }

    Integer getMaxLikesNum(List<Comment> comments) {
        Integer max = 0;
        for (Comment comment : comments) {
            Integer maxNum = comment.getLikesNum();
            if(maxNum > max)
            {
                max = maxNum;
            }
        }
        return max;
    }
}
