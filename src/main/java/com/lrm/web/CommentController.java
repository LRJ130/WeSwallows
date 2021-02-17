package com.lrm.web;

import com.lrm.po.Comment;
import com.lrm.po.User;
import com.lrm.service.CommentService;
import com.lrm.service.QuestionService;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController
{
    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;


    @GetMapping("/comments/{blogId}")
    public Result<Map<String, Object>> comments(@PathVariable Long blogId)
    {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }
    //提交表单后 到这里 然后得到id 然后刷新评论
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session)
    {
        User user = (User)session.getAttribute("user");
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        if (user !=null)
        {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            //           comment.setNickname(user.getNickname());
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }

}
