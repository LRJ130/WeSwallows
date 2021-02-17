package com.lrm.web;

import com.lrm.po.Question;
import com.lrm.service.QuestionService;
import com.lrm.service.TagService;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController
{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private TagService tagService;

    //首页返回推荐问题、全部问题
    @GetMapping("/")
    public Result<Map<String, Object>> index(@PageableDefault(size = 6, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("pages",questionService.listQuestion(pageable));
        hashMap.put("tags", tagService.listTagTop(10));
        hashMap.put("impactQuestions", questionService.listImpactQuestionTop(8));
        return new Result<>(hashMap, true, "");
    }

    //按输入搜索标题/内容
    @PostMapping("/search")
    public Result<Map<String, Object>> search(@PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
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
        question.setView(question.getView()+1);
        question.setImpact(question.getImpact()+question.getView()*2);
        return new Result<>(hashMap, true, "");
    }

}
