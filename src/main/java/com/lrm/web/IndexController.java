package com.lrm.web;

import com.lrm.service.QuestionService;
import com.lrm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController
{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String  index(@PageableDefault(size = 6, sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        Map<String,Object> hashMap = new HashMap<>();
        hashMap.put("page",questionService.listQuestion(pageable));
        hashMap.put("tags", tagService.listTagTop(10));
        hashMap.put("recommendQuestion", QuestionService.listRecommendQuestionTop(8));
        return "index" ;
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 1000, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model)
    {
        //mysql语句 模糊查询的格式 jpa不会帮处理string前后有没有%的
        model.addAttribute("page", QuestionService.listQuestion("%"+query+"%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/question/{id}")
    public String question (@PathVariable Long id, Model model)
    {
        model.addAttribute("question", questionService.getAndConvert(id));
        return "question";
    }

    @GetMapping("/footer/newquestion")
    public String newquestions(Model model)
    {
        model.addAttribute("newquestions", questionService.listRecommendquestionTop(3));
        return "_fragments :: newquestionList";
    }
}
