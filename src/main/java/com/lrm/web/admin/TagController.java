package com.lrm.web.admin;

import com.lrm.Exception.NotFoundException;
import com.lrm.po.Tag;
import com.lrm.service.TagService;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    //返回所有标签的分页
    @GetMapping("/tags")
    public Result<Map<String, Object>> tags(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC)
                               Pageable pageable) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("tags",tagService.listTag(pageable));
        return new Result<>(hashMap, true, "");
    }

    //新增标签
    @GetMapping("/tags/input")
    public Result<Map<String, Object>> input() {
        Map<String, Object> hashMap = new HashMap<>();
        Tag tag = new Tag();
        hashMap.put("tags", tag);
        return new Result<>(hashMap, true, "");
    }

    //修改标签
    @GetMapping("/tags/{tagId}/input")
    public Result<Map<String, Object>> editInput(@PathVariable Long id) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("tags", tagService.getTag(id));
        return new Result<>(hashMap, true, "");
    }

    @PostMapping("/tags")
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

    @GetMapping("/tags/{tagId}/delete")
    public Result<Map<String, Object>> delete(@PathVariable Long tagId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        Tag tag = tagService.getTag(tagId);
        if(tag == null)
        {
            throw new NotFoundException("该问题不存在");
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
