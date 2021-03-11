package com.lrm.web.customer;

import com.lrm.service.QuestionService;
import com.lrm.util.Methods;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class ArchiveShowController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/archives")
    public Result<Map<String, Object>> archives (HttpServletRequest request)
    {
        Map<String, Object> hashMap = new HashMap<>();
        Long userId = Methods.getCustomUserId(request);
        hashMap.put("archiveMap", questionService.archivesQuestion(userId));
        hashMap.put("QuestionCount", questionService.countQuestionByUser(userId));
        return new Result<>(hashMap, true, "");
    }
}
