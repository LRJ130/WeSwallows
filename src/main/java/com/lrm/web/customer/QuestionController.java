package com.lrm.web.customer;

import com.lrm.Exception.NotFoundException;
import com.lrm.po.Question;
import com.lrm.po.Tag;
import com.lrm.service.QuestionService;
import com.lrm.service.TagService;
import com.lrm.service.UserService;
import com.lrm.vo.QuestionQuery;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//应该有一个拦截session中userid和url中userid不符的manage并抛出异常 这里是它俩相等的前提 在这样的前提下 不用考虑用户是否存在的异常
@RequestMapping("/customer/{userId}")
@RestController
public class QuestionController
{
    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    //后台返回个人所发问题的列表
    @GetMapping("/questions")
    //因为有一堆数据，所以查询条件封装成QuestionQuery了
    public Result<Map<String, Object>> Questions(@PageableDefault(size = 6, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                                         QuestionQuery question, @PathVariable Long userId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        //这个页面的查找功能 得给前端所有的tag
        hashMap.put("tags", tagService.listTag());
        hashMap.put("pages", questionService.listQuestionPlusUserId(pageable, question, userId));
        return new Result<>(hashMap, true, "");
    }

    //个人主页搜索 根据分级标签 标题 返回个人发出的问题
    @PostMapping("/questions/search")
    public Result<Map<String, Object>> search(@PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         QuestionQuery question, @PathVariable Long userId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("tags", tagService.listTag());
        hashMap.put("pages", questionService.listQuestionPlusUserId(pageable, question, userId));
        return new Result<>(hashMap, true, "搜索完成");
    }


    //有初始化的作用 所有属性都是null
    @GetMapping("/questions/input")
    public Result<Map<String, Object>> input()
    {
        Map<String, Object> hashMap= new HashMap<>();
        Question question = new Question();
        List<Tag> tags = tagService.listTag();
        hashMap.put("questions", question);
        hashMap.put("tags", tags);
        return new Result<>(hashMap, true, "");
    }

    //新增问题 初始化各部分属性
    @PostMapping("/questions")
    public Result<Map<String, Object>> post(@Valid Question question, BindingResult bindingResult, @PathVariable Long userId)
    {
        Map<String, Object> hashMap= new HashMap<>();
        //后端检验valid 如果校验失败 返回input页面
        if(bindingResult.hasErrors())
        {
            hashMap.put("questions", question);
            return new Result<>(hashMap, false, "标题、内容、概述均不能为空");
        }
        question.setUser(userService.getUser(userId));
        //令前端只传回tagIds而不是tag对象 将它转换为List<Tag> 在service层找到对应的Tag保存到数据库
        question.setTags(tagService.listTag(question.getTagIds()));
        Question q;

        if(question.getId() == null)
        {
            q = questionService.saveQuestion(question);
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

    //删除问题
    @GetMapping("/questions/{questionId}/delete")
    public Result<Map<String, Object>> delete(@PathVariable Long questionId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        Question question = questionService.getQuestion(questionId);
        if(question == null)
        {
            throw new NotFoundException("该问题不存在");
        }
        questionService.deleteQuestion(questionId);
        question = questionService.getQuestion(questionId);
        if(question != null)
        {
            hashMap.put("questions", question);
            return new Result<>(hashMap, false, "删除失败");
        } else {
            return new Result<>(null, true, "删除成功");
        }
    }
}
