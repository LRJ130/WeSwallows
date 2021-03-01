package com.lrm.web.customer;

import com.lrm.po.Comment;
import com.lrm.po.Likes;
import com.lrm.service.CommentService;
import com.lrm.service.LikesService;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/customer/{userId}/messages")
@RestController
public class MessageController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikesService likesService;

    //返回所有通知
    public Result<Map<String, Object>> messages(@PathVariable Long userId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        List<Comment> comments = commentService.listAllNotReadComment(userId);
        List<Likes> likes = likesService.listAllNotReadComment(userId);
        hashMap.put("Comments", comments);
        hashMap.put("Likes", likes);
        return new Result<>(hashMap, true, "");
    }

    //两个单独已读
    @GetMapping("/{commentId}/read")
    public void readComment(@PathVariable Long commentId) {
        Comment comment = commentService.getComment(commentId);
        comment.setRead(true);
    }
    @GetMapping("/{likesId}/read")
    public void readLikes(@PathVariable Long likesId) {
        Likes likes = likesService.getLikes(likesId);
        likes.setRead(true);
    }

    //两个全部已读
    @GetMapping("/readAllComments")
    public void readAllComments(List<Comment> comments) {
        for(Comment comment : comments) {
            comment.setRead(true);
        }
    }
    @GetMapping("/readAllLikes")
    public void readAllLikes(List<Likes> likes){
        for(Likes likes1 : likes)
        {
            likes1.setRead(true);
        }
    }
}

