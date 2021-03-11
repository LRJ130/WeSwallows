package com.lrm.web;

import com.lrm.po.Likes;
import com.lrm.po.Question;
import com.lrm.po.User;
import com.lrm.service.LikesService;
import com.lrm.service.QuestionService;
import com.lrm.service.UserService;
import com.lrm.util.Methods;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController
{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikesService likesService;

    //首页返回推荐问题、全部问题
        //怎么显示有没有点过赞呢？现在不太明白...只能用计算力代替了
    @GetMapping("/")
    public Result<Map<String, Object>> index(@PageableDefault(size = 6, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                                             HttpServletRequest request)
    {

        Map<String,Object> hashMap = new HashMap<>();
        Page<Question> page = questionService.listQuestion(pageable);
        Long userId = Methods.getCustomUserId(request);
        List<Boolean> approved = new ArrayList<>();
        for(Question question : page)
        {
            if(likesService.getLikes(userService.getUser(userId), question) != null)
            {
                approved.add(true);
            } else {
                approved.add(false);
            }
        }
        hashMap.put("pages",questionService.listQuestion(pageable));
        hashMap.put("approved", approved);
        hashMap.put("impactQuestions", questionService.listImpactQuestionTop(8));
        return new Result<>(hashMap, true, "");
    }

    //按输入搜索标题/内容
    @PostMapping("/search")
    public Result<Map<String, Object>> search(@PageableDefault(size = 1000, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         String query)
    {
        //mysql语句 模糊查询的格式 jpa不会帮处理string前后有没有%的
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("pages", questionService.listQuestion("%"+query+"%", pageable));
        //还要传回 保证在新的查询页面 查询框中也有自己之前查询的条件的内容
        hashMap.put("queries", query);
        return new Result<>(hashMap, true, "");
    }

    //问题内容展示
    @GetMapping("/question/{questionId}")
    public Result<Map<String, Object>> question (@PathVariable Long questionId)
    {
        Map<String,Object> hashMap = new HashMap<>();
        //返回markdown格式
        //hashMap.put("question", questionService.getAndConvert(id));
        Question question = questionService.getQuestion(questionId);
        //每多1次浏览，问题影响力+2
        question.setView(question.getView()+1);
        question.setImpact(question.getImpact()+2);
        hashMap.put("questions", question);
        return new Result<>(hashMap, true, "");
    }

    //点赞
    @GetMapping("/question/{questionId}/approve")
    public void approve(@PathVariable Long questionId, HttpServletRequest request)
    {
        Long postUserId = Methods.getCustomUserId(request);
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

    //点踩
    @GetMapping("/question/{questionId}/disapprove/")
    public void  disapproved(@PathVariable Long questionId)
    {
        Question question = questionService.getQuestion(questionId);
        question.setDisLikesNum(question.getDisLikesNum()+1);
        if(question.getDisLikesNum() >= 6 & (question.getLikesNum() <= 2 * question.getDisLikesNum()))
        {
            question.setHidden(true);
        }
    }
}
