package com.lrm.web;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lrm.po.Comment;
import com.lrm.po.User;
import com.lrm.service.CommentService;
import com.lrm.service.UserService;
import com.lrm.util.JWTUtils;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CommentController
{
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    //ajax
    @GetMapping("/comments/{questionId}")
    public Result<Map<String, Object>> comments(@PathVariable Long questionId)
    {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("comments", commentService.listCommentByQuestionId(questionId));
        return new Result<>(hashMap, true, "");
    }

    //提交表单后 到这里 然后得到id 然后刷新评论
    @PostMapping("/comments")
    public Result<Map<String, Object>> post(Comment comment, HttpServletRequest request)
    {
        Map<String, Object> hashMap= new HashMap<>();
        String token = request.getHeader("token");
        DecodedJWT decodedJWT = JWTUtils.getToken(token);
        Long userId = decodedJWT.getClaim("userId").asLong();
        User user = userService.getUser(userId);
        Long questionId = comment.getQuestion().getId();

        //直接保存不setQuestsion的话 只保存了questionId 其他的信息没有保存 似乎也可以 那么是不是mybatis更好呢...只用id就可以满足需要了...
        comment.setPostUser(user);
        commentService.saveComment(comment, questionId);

        if(commentService.getComment(comment.getId()) != null)
        {
            hashMap.put("comments",comment);
            return new Result<>(hashMap, true,"发布成功");
        } else
        {
            return new Result<>(null, false, "发布失败");
        }
    }

}
