package com.lrm.web.customer;

import com.lrm.po.Question;
import com.lrm.po.Tag;
import com.lrm.po.User;
import com.lrm.service.QuestionService;
import com.lrm.service.TagService;
import com.lrm.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/customer")
@RestController
public class QuestionController
{
    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionService QuestionService;

    //后台返回问题列表
    //@GetMapping("/admin/Questions")
    //因为有一堆数据，所以查询条件封装成QuestionQuery了
    //public Map<String, List<Object>> Questions(@PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
    //                                          QuestionQuery Question, Model model)
    //{
    //    Map<String, List<Object>> hashmap = new HashMap<>();
    //    hashmap.put("types", typeService.listType());
    //    model.addAttribute("page", QuestionService.listQuestion(pageable, Question));
    //    return LIST;
    //}

    //搜索
//    @PostMapping("/Questions/search")
//    public String search(@PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
//                         QuestionQuery Question, Model model)
//    {
//        model.addAttribute("page", QuestionService.listQuestion(pageable, Question));
//        return "admin/Questions :: QuestionList";
//    }
//
//    private  void setTypeAndTag(Model model)
//    {
//        model.addAttribute("types", typeService.listType());
//        model.addAttribute("tags", tagService.listTag());
//    }

    //有初始化的作用 所有属性都是null
    @GetMapping("/Questions/input")
    public Map<String, Object> input()
    {
        Map<String, Object> hashMap= new HashMap<>();
        Question question = new Question();
        List<Tag> tags = tagService.listTag();
        hashMap.put("Question", question);
        hashMap.put("Tags", tags);
        return hashMap;
    }

    @GetMapping("/Questions/{id}/input")
    public Map<String, Object> editInput(@PathVariable Long id)
    {
        Map<String, Object> hashMap= new HashMap<>();
        Question question= QuestionService.getQuestion(id);
        question.init();
        List<Tag> tags = tagService.listTag();
        hashMap.put("Question", question);
        hashMap.put("Tags", tags);
        return hashMap;
    }

    @PostMapping("/Questions")
    public R<Question> post(Question question, HttpSession session)
    {
        question.setUser((User)session.getAttribute("user"));
        //将前端传回的tagIds转换为List<tag> 保存到数据库
        question.setTags(tagService.listTag(question.getTagIds()));
        //保存重复属性会覆盖原有的 没有被覆盖的保持
        Question q;
        //在Questions-input中没有createTime和view的隐含域 所以传到controller里的Question中是null的
        //所以updateQuestion要通过id找到原来的Question 这个Question有createTim和view的值 然后更新一下Question 这样就不是null了！
        if(question.getId() == null)
        {
            q = QuestionService.saveQuestion(question);
        } else {
            q = QuestionService.updateQuestion(question.getId(), question);
        }

        if (q == null)
        {
            return new R<>(null, false, "操作失败");
        } else {
            return new R<>(q, true,"操作成功");
        }
    }

    @GetMapping("/Questions/{id}/delete")
    public void delete(@PathVariable Long id)
    {
        QuestionService.deleteQuestion(id);
    }
}
