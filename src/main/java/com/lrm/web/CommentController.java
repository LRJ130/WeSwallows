package com.lrm.web;

import com.lrm.Exception.NoPermissionException;
import com.lrm.Exception.NotFoundException;
import com.lrm.po.Comment;
import com.lrm.po.Likes;
import com.lrm.po.Question;
import com.lrm.po.User;
import com.lrm.service.CommentService;
import com.lrm.service.LikesService;
import com.lrm.service.QuestionService;
import com.lrm.service.UserService;
import com.lrm.util.Methods;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public Result<Map<String, Object>> comments(@PathVariable Long questionId,  HttpServletRequest request)
    {
        Map<String,Object> hashMap = new HashMap<>();
        Long userId = Methods.getCustomUserId(request);
        //分别返回两类评论和对应点赞
        List<Comment> comments1 = commentService.listCommentByQuestionId(questionId, false);
        List<Boolean> approved1 = new ArrayList<>();
        for(Comment comment : comments1)
        {
            if(likesService.getLikes(userService.getUser(userId), comment) != null)
            {
                approved1.add(true);
            } else {
                approved1.add(false);
            }
        }
        hashMap.put("approved1", approved1);
        hashMap.put("comments1", comments1);

        List<Comment> comments2 = commentService.listCommentByQuestionId(questionId, true);
        List<Boolean> approved2 = new ArrayList<>();
        for(Comment comment : comments2)
        {
            if(likesService.getLikes(userService.getUser(userId), comment) != null)
            {
                approved2.add(true);
            } else {
                approved2.add(false);
            }
        }
        hashMap.put("approved2", approved2);
        hashMap.put("comments2", commentService.listCommentByQuestionId(questionId, true));

        return new Result<>(hashMap, true, "");
    }

    //提交表单后 到这里 然后得到id 然后刷新评论
    @PostMapping("/comments")
    public Result<Map<String, Object>> post(@Valid Comment comment, HttpServletRequest request, BindingResult bindingResult)
    {
        Map<String, Object> hashMap= new HashMap<>();
        Long userId = Methods.getCustomUserId(request);
        User postUser = userService.getUser(userId);
        Long questionId = comment.getQuestion().getId();
        //如果是空报错
        if(bindingResult.hasErrors())
        {
            hashMap.put("comments", comment);
            return new Result<>(hashMap, false, "内容不能为空");
        }
        commentService.saveComment(comment, questionId, postUser);
        questionService.getQuestion(questionId).setNewCommentedTime(new Date());
        //如果有了，更新发布时间。
        if(commentService.getComment(comment.getId()) != null)
        {
            questionService.getQuestion(questionId).setNewCommentedTime(new Date());
            hashMap.put("comments",comment);
            return new Result<>(hashMap, true,"发布成功");
        } else
        {
            return new Result<>(null, false, "发布失败");
        }
    }

    //删除评论
    @GetMapping("/comment/{commentId}/delete")
    public Result<Map<String, Object>> deleteComment(@PathVariable Long commentId, HttpServletRequest request)
    {
        Map<String, Object> hashMap = new HashMap<>();
        User customUser = userService.getUser(Methods.getCustomUserId(request));
        Boolean admin = Methods.isAdmin(request);
        Comment comment = commentService.getComment(commentId);
        //如果评论不存在&没权限删除评论报错
        if(comment == null)
        {
            throw new NotFoundException("该评论不存在");
        }
        if((comment.getPostUser() != customUser) & (!admin))
        {
            throw new NoPermissionException("您无权限删除该评论");
        }
        commentService.deleteComment(commentId);
        comment = commentService.getComment(commentId);
        if(comment != null)
        {
            hashMap.put("comments", comment);
            return new Result<>(hashMap, false, "删除失败");
        } else {
            return new Result<>(null, true, "删除成功");
        }
    }

    //点赞
    @GetMapping("/comment/{commentId}/approve")
    public void approve(@PathVariable Long questionId, @PathVariable Long commentId, HttpServletRequest request)
    {
        Comment comment = commentService.getComment(commentId);
        //只能给有效问题点赞
        if(comment.getAnswer()) {
            Long postUserId = Methods.getCustomUserId(request);
            User postUser = userService.getUser(postUserId);
            User receiveUser = comment.getReceiveUser();
            Likes likes = likesService.getLikes(postUser, comment);
            //有则删除，无则增加
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
                question.setImpact(question.getImpact() + 2 * (maxNum1-maxNum0) + 12);
            }
        }
    }

    //点踩
    @GetMapping("/comment/{commentId}/disapprove/")
    public void  disapproved(@PathVariable Long commentId)
    {
        Comment comment = commentService.getComment(commentId);
        comment.setDisLikesNum(comment.getDisLikesNum()+1);
        //符合规则就隐藏
        if(comment.getDisLikesNum() >= 6 & (comment.getLikesNum() <= 2 * comment.getDisLikesNum()))
        {
            comment.setHidden(true);
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

    /**
     * @param files 多文件上传
     * @param questionId 发布问题的Id
     * @return 多文件在本地的路径
     * @throws IOException 文件大小溢出
     */
    @PostMapping("/uploadPhotos")
    public Result<Map<String, Object>> uploadPhotos(MultipartFile[] files, HttpServletRequest req, @PathVariable Long questionId, @RequestParam Long commentId) throws IOException {
        Map<String, Object> hashMap= new HashMap<>();
        //创建存放文件的文件夹的流程
        Long userId = Methods.getCustomUserId(req);
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy-MM-dd/");
        String format = sdf.format(new Date());
        String path = "/upload/" + userId + "/questions/" + questionId + "/comments/" + format;
        //新文件夹目录绝对路径
        String realPath = req.getServletContext().getRealPath(path);
        List<String> pathList = new ArrayList<String>();
        for (MultipartFile uploadFile : files)
        {
            File folder = new File(realPath);
            if (!folder.isDirectory()){
                folder.mkdirs();
            }
            //保存文件到文件夹中
            //所上传的文件原名
            String oldName = uploadFile.getOriginalFilename();
            //新文件名
            String newName = UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."));
            uploadFile.transferTo(new File(folder, newName));
            pathList.add(realPath + newName);
        }

        hashMap.put("photos", pathList);
        return new Result<>(hashMap, true, "上传成功");
    }
}
