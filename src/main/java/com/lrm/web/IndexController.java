package com.lrm.web;

import com.lrm.po.Likes;
import com.lrm.po.Question;
import com.lrm.po.Tag;
import com.lrm.po.User;
import com.lrm.service.LikesService;
import com.lrm.service.QuestionService;
import com.lrm.service.TagService;
import com.lrm.service.UserService;
import com.lrm.util.FileControl;
import com.lrm.util.GetTokenInfo;
import com.lrm.vo.Magic;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class IndexController
{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private TagService tagService;

    /**
     * 怎么显示有没有点过赞呢？现在不太明白...只能用计算力代替了.
     *
     * @param pageable 分页.
     * @return 返回推荐问题、全部问题、问题对应用户是否点赞.
     */
    @GetMapping("/")
    public Result<Map<String, Object>> index(@PageableDefault(size = 7, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                                             HttpServletRequest request) {
        Map<String, Object> hashMap = new HashMap<>(3);
        Page<Question> page = questionService.listQuestion(pageable);
        Long userId = GetTokenInfo.getCustomUserId(request);
        User postUser = userService.getUser(userId);
        for (Question question : page) {   //可简化如下 但为逻辑清晰这样写
            //question.setApproved(likesService.getLikes(userService.getUser(userId), question) != null);
            if (likesService.getLikes(userService.getUser(userId), question) != null) {
                question.setApproved(true);
            } else {
                question.setApproved(false);
            }
            //这里到底要不要用计算力代替空间还要考虑
            question.setAvatar(postUser.getAvatar());
            question.setNickname(postUser.getNickname());
        }
        hashMap.put("pages", page);
        hashMap.put("impactQuestions", questionService.listImpactQuestionTop(Magic.RECOMMENDED_QUESTIONS_SIZE));
        return new Result<>(hashMap, true, "");
    }

    /**
     * 按输入搜索标题/内容.
     *
     * @param pageable 分页
     * @param query    查询条件.
     * @return 查询结果、查询条件.
     */
    @PostMapping("/searchQuestion")
    public Result<Map<String, Object>> search(@PageableDefault(size = 1000, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                                              String query) {
        //mysql语句 模糊查询的格式 jpa不会帮处理string前后有没有%的
        Map<String, Object> hashMap = new HashMap<>(2);
        hashMap.put("pages", questionService.listQuestion("%" + query + "%", pageable));
        //还要传回 保证在新的查询页面 查询框中也有自己之前查询的条件的内容
        hashMap.put("queries", query);
        return new Result<>(hashMap, true, "");
    }

    /**
     * 问题内容展示.
     * @param questionId 问题Id.
     * @return 问题的内容.
     */
    @GetMapping("/question/{questionId}")
    public Result<Map<String, Object>> question (@PathVariable Long questionId)
    {
        Map<String,Object> hashMap = new HashMap<>(1);
        //返回markdown格式
        Question question = questionService.getAndConvert(questionId);
        hashMap.put("questions", question);
        return new Result<>(hashMap, true, "");
    }

    /**
     * 点赞.
     * @param questionId 问题Id.
     */
    @GetMapping("/question/{questionId}/approve")
    public void approve(@PathVariable Long questionId, HttpServletRequest request) {
        Long postUserId = GetTokenInfo.getCustomUserId(request);
        Question question = questionService.getQuestion(questionId);
        User postUser = userService.getUser(postUserId);
        User receiveUser = question.getUser();
        Likes likes = likesService.getLikes(postUser, question);
        if(likes != null)
        {
            likesService.deleteLikes(likes);
        }
        else {
            Likes likes1 = new Likes();
            likes1.setLikeQuestion(true);
            likes1.setLikeComment(false);
            question.setLikesNum(question.getLikesNum() + 1);
            likesService.saveLikes(likes1, postUser, receiveUser);
            //问题被点赞 提问者贡献值+2
            receiveUser.setDonation(receiveUser.getDonation() + 2);
            //提问者贡献值对问题影响力+8 点赞本身+2
            question.setImpact(question.getImpact() + 2 + 8);
        }
    }


    /**
     * 点踩.
     * @param questionId 问题Id.
     */
    @GetMapping("/question/{questionId}/disapprove/")
    public void  disapproved(@PathVariable Long questionId)
    {
        Question question = questionService.getQuestion(questionId);
        question.setDisLikesNum(question.getDisLikesNum() + 1);
        if((question.getDisLikesNum() >= Magic.HIDE_STANDARD1) & (question.getLikesNum() <= Magic.HIDE_STANDARD2 * question.getDisLikesNum()))
        {
            question.setHidden(true);
        }
    }

    /* 首页发布问题 */

    /**
     * 有初始化的作用 所有属性都是null.
     * @return questions:新question对象 tags:第一级标签.
     */
    @GetMapping("/questions/input")
    public Result<Map<String, Object>> input() {
        Map<String, Object> hashMap = new HashMap<>(2);
        Question question = new Question();
        List<Tag> tags = tagService.listTagTop();
        hashMap.put("questions", question);
        hashMap.put("tags", tags);
        return new Result<>(hashMap, true, "");
    }

    /**
     * 新增问题 初始化各部分属性.
     * @param question 前端封装的question对象
     * @param bindingResult 配合@Valid检测是否为空.
     * @return 报错信息/成功信息.
     */
    @PostMapping("/questions")
    public Result<Map<String, Object>> post(@Valid Question question, BindingResult bindingResult, HttpServletRequest request) {
        Map<String, Object> hashMap = new HashMap<>(1);
        Long userId = GetTokenInfo.getCustomUserId(request);
        //后端检验valid 如果校验失败 返回input页面
        if(bindingResult.hasErrors())
        {
            hashMap.put("questions", question);
            return new Result<>(hashMap, false, "标题、内容、概述均不能为空");
        }
        User user = userService.getUser(userId);
        question.setUser(user);
        //令前端只传回tagIds而不是tag对象 将它转换为List<Tag> 在service层找到对应的Tag保存到数据库
        question.setTags(tagService.listTag(question.getTagIds()));
        Question q;

        if(question.getId() == null)
        {
            q = questionService.saveQuestion(question, user);
        }else
        {
            hashMap.put("questions", question);
            return new Result<>(hashMap, false, "该问题已存在");
        }

        if (q == null)
        {
            return new Result<>(null, false, "发布失败");
        } else {
            hashMap.put("questions", question);
            return new Result<>(hashMap, true,"发布成功");
        }
    }

    /**
     * @param parentTagId 上一级标签Id.
     * @return 下一级标签集合.
     */
    @GetMapping("/questions/{parentTagId}/nextTag")
    public Result<Map<String, Object>> showNext(@PathVariable Long parentTagId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("tags", tagService.getTag(parentTagId).getSonTags());
        return new Result<>(hashMap, true, "");
    }

    /**
     * @param files 多文件上传
     * @param questionId 发布问题的Id.
     * @return 多文件在本地的路径.
     * @throws IOException 文件大小溢出.
     */
    @PostMapping("/questions/uploadPhotos")
    public Result<Map<String, Object>> uploadPhotos(MultipartFile[] files, HttpServletRequest req, @RequestParam Long questionId) throws IOException {
        Map<String, Object> hashMap= new HashMap<>(files.length);
        //创建存放文件的文件夹的流程
        Long userId = GetTokenInfo.getCustomUserId(req);
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy-MM-dd/");
        String format = sdf.format(new Date());
        String path = "/upload/" + userId + "/questions/" + questionId + format;
        //新文件夹目录绝对路径
        String realPath = req.getServletContext().getRealPath(path);
        File folder = new File(req.getServletContext().getRealPath("/upload/" + userId + "/questions/" + questionId));
        //如果文件夹不存在，创建文件夹 否则删除文件夹
        if (folder.exists())
        {
            FileControl.deleteFile(folder);
        }
        List<String> pathList = new ArrayList<>();
        for (MultipartFile uploadFile : files)
        {
            folder = new File(realPath);
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
