package com.lrm.web.admin;

import com.lrm.Exception.NotFoundException;
import com.lrm.po.Tag;
import com.lrm.service.TagService;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 山水夜止.
 */
@RestController
@RequestMapping("/admin/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * @return 返回所有第一级标签.
     */
    @GetMapping("/")
    public Result<Map<String, Object>> tags() {
        Map<String, Object> hashMap = new HashMap<>(1);
        hashMap.put("tags", tagService.listTagTop());
        return new Result<>(hashMap, true, "");
    }

    /**
     * @param parentTagId 上一级标签Id.
     * @return 返回下一级标签.
     */
    @GetMapping("/{parentTagId}/nextTag")
    public Result<Map<String, Object>> showNext(@PathVariable Long parentTagId)
    {
        Map<String, Object> hashMap = new HashMap<>(1);
        hashMap.put("tags", tagService.getTag(parentTagId).getSonTags());
        return new Result<>(hashMap, true, "");
    }

    //采用评论区的js方法
    //新增第一级标签
//    @GetMapping("/tags/input")
//    public Result<Map<String, Object>> input() {
//        Map<String, Object> hashMap = new HashMap<>();
//        Tag tag = new Tag();
//        hashMap.put("tags", tag);
//        return new Result<>(hashMap, true, "");
//    }

    //如果我这里不使用类似评论区的js功能——直接在本页面修改封装成form表单 而是通过getMapping获得返回的新的Tag 是不是必须要在这里直接设置parentTag？
    //否则会丢失这个parentCommentId
    //新增下级标签
//    @GetMapping("/tags/{tagId}/add")
//    public Result<Map<String, Object>> add() {
//        Map<String, Object> hashMap = new HashMap<>();
//        Tag tag = new Tag();
//        hashMap.put("tags", tag);
//        return new Result<>(hashMap, true, "");
//    }

    //修改标签
//    @GetMapping("/tags/{tagId}/input")
//    public Result<Map<String, Object>> editInput(@PathVariable Long tagId) {
//        Map<String, Object> hashMap = new HashMap<>();
//        hashMap.put("tags", tagService.getTag(tagId));
//        return new Result<>(hashMap, true, "");
//    }

    /**
     * @param tag 前端封装好的Tag对象.
     * @return 返回报错信息; 已保存的Tag对象.
     */
    @PostMapping("/")
    public Result<Map<String, Object>> post(@Valid Tag tag, BindingResult result) {
        Map<String, Object> hashMap = new HashMap<>();
        //返回input页面的错误提示
        if (result.hasErrors()) {
            return new Result<>(hashMap, false, "标签名不能为空");
        }
        //检查是否存在同名标签 注意不区分大小写
        Tag tag0 = tagService.getTagByName(tag.getName());
        if (tag0 != null) {
            hashMap.put("tags", tag);
            return new Result<>(hashMap, false, "不能添加重复的标签");
        }
        //数据库中同id的tag 检查是新增还是修改
        Tag tag1 = tagService.getTag(tag.getId());
        if(tag1 == null) {
            Tag t = tagService.saveTag(tag);
            if (t == null) {
                return new Result<>(hashMap, false, "新增失败");
            } else {
                return new Result<>(hashMap, true, "新增成功");
            }
        }
        //如果是修改
        Tag t = tagService.updateTag(tag);
        if (t == null) {
            return new Result<>(hashMap, false, "修改失败");
        } else {
            return new Result<>(hashMap, true, "修改成功");
        }
    }

    /**
     * 删除标签.
     */
    @GetMapping("/{tagId}/delete")
    public Result<Map<String, Object>> delete(@PathVariable Long tagId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        Tag tag = tagService.getTag(tagId);
        if(tag == null)
        {
            throw new NotFoundException("该标签不存在");
        }

        tagService.deleteTag(tagId);
        tag = tagService.getTag(tagId);
        if(tag != null)
        {
            hashMap.put("tags", tag);
            return new Result<>(hashMap, false, "删除失败");
        } else {
            return new Result<>(null, true, "删除成功");
        }
    }


}
