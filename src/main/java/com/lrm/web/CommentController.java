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
import com.lrm.util.GetTokenInfo;
import com.lrm.vo.Magic;
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

/**
 * @author 山水夜止.
 */
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

    /**
     * 展示所有评论
     *
     * @param request 用于得到当前userId 处理当前用户点没点过赞的
     * @return 第一类评论+对应在线用户是否点过赞、第二类评论+对应在线用户是否点过赞
     */
    @GetMapping("/comments")
    public Result<Map<String, Object>> comments(@PathVariable Long questionId, HttpServletRequest request) {
        Map<String, Object> hashMap = new HashMap<>(4);

        Long userId = GetTokenInfo.getCustomUserId(request);

        //分别返回两类评论和对应点赞
        List<Comment> comments1 = commentService.listCommentByQuestionId(questionId, false);

        hashMap.put("comments1", dealComment(comments1, userId));

        List<Comment> comments2 = commentService.listCommentByQuestionId(questionId, true);

        hashMap.put("comments2", dealComment(comments2, userId));

        return new Result<>(hashMap, true, "");
    }

    /**
     * 新增评论
     * 提交表单后 到这里 然后得到id 然后刷新评论
     *
     * @return 新增的评论或新增失败报错
     */
    @PostMapping("/comments")
    public Result<Map<String, Object>> post(@Valid Comment comment, HttpServletRequest request, BindingResult bindingResult) {
        Map<String, Object> hashMap = new HashMap<>(1);
        Long userId = GetTokenInfo.getCustomUserId(request);
        User postUser = userService.getUser(userId);
        Long questionId = comment.getQuestion().getId();

        //如果是空 报错
        if (bindingResult.hasErrors()) {
            hashMap.put("comments", comment);
            return new Result<>(hashMap, false, "内容不能为空");
        }

        //保存
        commentService.saveComment(comment, questionId, postUser);

        //如果有了，更新发布时间
        if (commentService.getComment(comment.getId()) != null) {
            questionService.getQuestion(questionId).setNewCommentedTime(new Date());

            hashMap.put("comments", comment);

            return new Result<>(hashMap, true, "发布成功");
        } else {
            return new Result<>(null, false, "发布失败");
        }
    }

    /**
     * 删除评论
     *
     * @return 报错信息
     */
    @GetMapping("/comment/{commentId}/delete")
    public Result<Map<String, Object>> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        Map<String, Object> hashMap = new HashMap<>(1);
        User customUser = userService.getUser(GetTokenInfo.getCustomUserId(request));
        Boolean admin = GetTokenInfo.isAdmin(request);
        Comment comment = commentService.getComment(commentId);

        //如果评论不存在&没权限删除评论报错
        if (comment == null) {
            throw new NotFoundException("该评论不存在");
        }
        if ((comment.getPostUser() != customUser) & (!admin)) {
            throw new NoPermissionException("您无权限删除该评论");
        }

        commentService.deleteComment(commentId);
        comment = commentService.getComment(commentId);

        if (comment != null) {
            hashMap.put("comments", comment);
            return new Result<>(hashMap, false, "删除失败");
        } else {
            return new Result<>(null, true, "删除成功");
        }
    }

    /**
     * 点赞.
     */
    @GetMapping("/comment/{commentId}/approve")
    public void approve(@PathVariable Long questionId, @PathVariable Long commentId, HttpServletRequest request) {
        Comment comment = commentService.getComment(commentId);

        //只能给有效问题点赞
        if (comment.getAnswer()) {
            Long postUserId = GetTokenInfo.getCustomUserId(request);
            User postUser = userService.getUser(postUserId);
            User receiveUser = comment.getReceiveUser();
            Likes likes = likesService.getLikes(postUser, comment);

            //有则删除，无则增加
            if (likes != null) {
                likesService.deleteLikes(likes);
            } else {
                Likes likes1 = new Likes();

                //因为saveLikes是问题点赞和评论点赞公用的 所以要在这里写
                likes1.setLikeQuestion(false);
                likes1.setLikeComment(true);

                //点赞前的最高赞数
                Integer maxNum0 = getMaxLikesNum(commentService.listAllCommentByQuestionId(questionId));
                likesService.saveLikes(likes1, postUser, receiveUser);
                comment.setLikesNum(comment.getLikesNum() + 1);
                likes1.setComment(comment);

                //问题被点赞 提问者贡献值+3
                receiveUser.setDonation(receiveUser.getDonation() + 3);

                //提问者贡献值对问题影响力+12
                //点赞后的最高赞数
                Integer maxNum1 = getMaxLikesNum(commentService.listAllCommentByQuestionId(questionId));
                Question question = questionService.getQuestion(questionId);
                question.setImpact(question.getImpact() + 2 * (maxNum1 - maxNum0) + 12);
            }
        }
    }

    /**
     * @param comments 评论集合
     * @return 集合中评论被点赞最多的这个点赞数
     */
    Integer getMaxLikesNum(List<Comment> comments) {
        Integer max = 0;
        for (Comment comment : comments) {
            Integer maxNum = comment.getLikesNum();
            if (maxNum > max) {
                max = maxNum;
            }
        }
        return max;
    }

    /**
     * 点踩 到标准就隐藏
     * @param commentId 评论Id
     */
    @GetMapping("/comment/{commentId}/disapprove/")
    public void  disapproved(@PathVariable Long commentId) {
        Comment comment = commentService.getComment(commentId);
        comment.setDisLikesNum(comment.getDisLikesNum()+1);

        //符合规则就隐藏
        if((comment.getDisLikesNum() >= Magic.HIDE_STANDARD1) & (comment.getLikesNum() <= Magic.HIDE_STANDARD2 * comment.getDisLikesNum())) {
            comment.setHidden(true);
        }
    }

    /**
     * 评论上传的图片
     *
     * @param files      多文件上传
     * @param questionId 发布问题的Id
     * @return 多文件在本地的路径
     * @throws IOException 文件大小溢出
     */
    @PostMapping("/uploadPhotos")
    public Result<Map<String, Object>> uploadPhotos(MultipartFile[] files, HttpServletRequest req, @PathVariable Long questionId, @RequestParam Long commentId) throws IOException {
        Map<String, Object> hashMap = new HashMap<>(1);

        //创建存放文件的文件夹的流程
        Long userId = GetTokenInfo.getCustomUserId(req);
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy-MM-dd/");
        String format = sdf.format(new Date());
        String path = "/upload/" + userId + "/questions/" + questionId + "/comments/" + format;

        //新文件夹目录绝对路径
        String realPath = req.getServletContext().getRealPath(path);
        List<String> pathList = new ArrayList<>();
        for (MultipartFile uploadFile : files) {
            File folder = new File(realPath);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }

            //保存文件到文件夹中

            //所上传的文件原名
            String oldName = uploadFile.getOriginalFilename();

            //新文件名
            String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
            uploadFile.transferTo(new File(folder, newName));
            pathList.add(realPath + newName);
        }

        hashMap.put("photos", pathList);
        return new Result<>(hashMap, true, "上传成功");
    }

    List<Comment> dealComment(List<Comment> comments, Long userId) {
        for (Comment comment : comments) {
            //得到发布问题的人
            User postUser = comment.getPostUser();

            if (likesService.getLikes(userService.getUser(userId), comment) != null) {
                comment.setApproved(true);
            } else {
                comment.setApproved(false);
            }

            //这里到底要不要用计算力代替空间还要考虑
            comment.setAvatar(postUser.getAvatar());
            comment.setNickname(postUser.getNickname());
        }
        return comments;
    }
}
