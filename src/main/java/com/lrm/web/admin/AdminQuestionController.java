package com.lrm.web.admin;

import com.lrm.po.Question;
import com.lrm.po.User;
import com.lrm.service.QuestionService;
import com.lrm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@RestController
public class AdminQuestionController
{
    private static final String INPUT = "admin/Questions-input";
    private static final String LIST = "admin/Questions";
    private static final String REDIRECT_LIST = "redirect:/admin/Questions";

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
    public String input(Model model)
    {
        setTypeAndTag(model);
        //id自增1，传递到前端了
        model.addAttribute("Question", new Question());
        return INPUT;
    }

    @GetMapping("/Questions/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        setTypeAndTag(model);
        Question Question= QuestionService.getQuestion(id);
        Question.init();
        model.addAttribute("Question", Question);
        return INPUT;
    }

    @PostMapping("/Questions")
    public String post(Question Question, RedirectAttributes attributes, HttpSession session)
    {
        Question.setUser((User)session.getAttribute("user"));
//      传入Question对象了根据Question获取type，为什么不直接赋过去，还要得到id，再得到type?
//      我觉得要用service就是因为与数据库挂钩，不然可能得不到这个映射的Type?
        Question.setType(typeService.getType(Question.getType().getId()));
        Question.setTags(tagService.listTag(Question.getTagIds()));
        //保存重复属性会覆盖原有的 没有被覆盖的保持
        Question b;
        //注意 在没更改之前 编辑和新增用的都是saveQuestion 结果是编辑之后 原本的createTime和view变成null了
        //原因：在Questions-input中没有createTime和view的隐含域 所以传到controller里的Question中是空的
        //此外 如果无隐含域 即使你先设了creat和view的值 传递到impl中的save语句的 Question对象中这俩也是空的
        //如果是新增 那么在impl的save语句中 id=null (Long类型的)条件下 有对它和updateTime的赋值语句
        //但是如果是编辑 id不是null 那么没有语句创建createTime和view 而且原有的也没了 所以就null了
        //所以整一个updateQuestion 通过id找到原来的Question 这个Question有createTim和view的值 然后更新一下Question 这样就不是null了！
        if(Question.getId() == null)
        {
            b = QuestionService.saveQuestion(Question);
        } else {
            b = QuestionService.updateQuestion(Question.getId(), Question);
        }

        if (b == null)
        {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return  REDIRECT_LIST;
    }

    @GetMapping("/Questions/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes)
    {
        QuestionService.deleteQuestion(id);
        attributes.addFlashAttribute("message","删除成功");
        return  REDIRECT_LIST;
    }
}
