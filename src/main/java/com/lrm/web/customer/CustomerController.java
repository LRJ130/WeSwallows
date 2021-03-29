package com.lrm.web.customer;

import com.lrm.po.User;
import com.lrm.service.UserService;
import com.lrm.util.FileControl;
import com.lrm.util.Methods;
import com.lrm.vo.Magic;
import com.lrm.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 山水夜止.
 */
@RequestMapping("/customer")
@RestController
public class CustomerController {
    @Autowired
    private UserService userService;

    /**
     * 返回个人信息.
     * @return user: 当前用户对象.
     */
    @GetMapping("/personal")
    public Result<Map<String, Object>> showMe(HttpServletRequest request)
    {
        Map<String, Object> hashMap = new HashMap<>(2);
        hashMap.put("user", userService.getUser(Methods.getCustomUserId(request)));
        hashMap.put("ACADEMIES", Magic.ACADEMIES);
        return new Result<>(hashMap, true, "");
    }

    //下面两个资料修改最好分开

    /**
     * 上传头像到本地 获取path返回.
     * @param file 被上传的文件.
     * @return avatar 文件在服务器端的路径.
     * @throws IOException 文件大小溢出.
     */
    @PostMapping("/uploadAvatar")
    public Result<Map<String, Object>> uploadAvatar(MultipartFile file, HttpServletRequest req) throws IOException {
        Map<String, Object> hashMap= new HashMap<>(1);
        //创建存放文件的文件夹的流程
        Long userId = Methods.getCustomUserId(req);
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy-MM-dd/");
        String format = sdf.format(new Date());
        //相对路径 名字含时间
        String path = "/upload/" + userId + "/avatar" + format;
        //新文件夹目录绝对路径
        String realPath = req.getServletContext().getRealPath(path);
        File folder = new File(req.getServletContext().getRealPath("/upload/" + userId + "/avatar"));
        File folder1 = new File(realPath);
        //如果头像文件夹不存在，创建文件夹 否则删除文件夹
        if (folder.exists())
        {
            FileControl.deleteFile(folder);
        }
        if (!folder1.exists()) {
          folder.mkdirs();
        }
        //保存文件到文件夹中
        //所上传的文件原名
        String oldName = file.getOriginalFilename();
        //新文件名
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        file.transferTo(new File(folder,newName));
        String absPath = realPath + "/" + newName;
        hashMap.put("avatar", absPath);
        userService.getUser(userId).setAvatar(absPath);
        return new Result<>(hashMap, true, "上传成功");
    }

    /**
     * 修改所有信息 封装成User返回.
     * @param user 旧用户对象.
     * @return user：新用户对象.
     */
    @PostMapping("/modifyAll")
    public Result<Map<String, Object>> modifyUserInformation(User user)
    {
        Map<String, Object> hashMap = new HashMap<>(1);
        hashMap.put("user", userService.updateUser(user));
        return new Result<>(hashMap, true, "修改成功");
    }




}
