package com.lrm.web.customer;

import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.vo.FileControl;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/customer/{userId}")
@RestController
public class CustomerController {
    @Autowired
    private UserService userService;

    //返回个人信息
    @GetMapping("/personal")
    public Result<Map<String, Object>> showMe(@PathVariable Long userId)
    {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", userService.getUser(userId));
        return new Result<>(hashMap, true, "");
    }

    //上传头像到本地 获取path返回
    @PostMapping("/uploadAvatar")
    public Result<Map<String, Object>> uploadAvatar(MultipartFile file, HttpServletRequest req) throws IOException {
        Map<String, Object> hashMap= new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        String format = sdf.format(new Date());
        //新文件夹目录 名字含时间
        String realPath = req.getServletContext().getRealPath("/upload") + format;
        File folder = new File(req.getServletContext().getRealPath("/upload"));
        File folder1 = new File(realPath);
        if (folder.exists())
        {
            FileControl.deleteFile(folder);
        }
        //如果文件夹不存在，创建文件夹 否则删除文件夹
        if (!folder1.exists()) {
          folder.mkdirs();
        }
        String oldName = file.getOriginalFilename(); //文件原名
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf(".")); //新文件名
        file.transferTo(new File(folder,newName));
        hashMap.put("avatar", realPath);
        return new Result<>(hashMap, true, "上传成功");
    }

    //修改所有信息 封装成User返回
    @PostMapping("/modifyAll")
    public Result<Map<String, Object>> modifyUserInformation(User user)
    {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", userService.updateUser(user));
        return new Result<>(hashMap, true, "修改成功");
    }


}
