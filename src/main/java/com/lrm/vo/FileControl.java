package com.lrm.vo;

import java.io.File;

public class FileControl {
     public static void deleteFile(File file) {
        if (file.exists())
        {//判断文件是否存在
            if (file.isFile())
            {//判断是否是文件
                file.delete();//删除文件
            } else if (file.isDirectory()) //否则如果它是一个目录
            {
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (File value : files)    //遍历目录下所有的文件
                {
                    deleteFile(value);//把每个文件用这个方法进行迭代
                }
                file.delete();//删除文件夹
            }
        }
    }
}
