package com.lrm.util;

import java.io.File;

public class FileControl {
     public static void deleteFile(File file) {
         //判断文件是否存在
         if (file.exists())
        {
            //判断是否是文件
            if (file.isFile())
            {
                //删除文件
                file.delete();
            //否则如果它是一个目录
            } else if (file.isDirectory())
            {
                //声明目录下所有的文件 files[];
                File[] files = file.listFiles();
                //遍历目录下所有的文件
                for (File value : files)
                {
                    //把每个文件用这个方法进行迭代
                    deleteFile(value);
                }
                //删除文件夹
                file.delete();
            }
        }
    }
}
