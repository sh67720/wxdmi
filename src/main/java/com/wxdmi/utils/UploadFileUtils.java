package com.wxdmi.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by ShenHua on 2017/11/18.
 */
public class UploadFileUtils {

    public static final String rootPath = "/root/images/"; //虚拟目录，对外展示图片的服务器地址
    public static final String httpPath = "http://img.shdmi.cn/"; //图片服务器的http地址
    //public static final String rootPath = "E:\\"; //虚拟目录，对外展示图片的服务器地址
    //public static final String httpPath = "file://E:/"; //图片服务器的http地址
    public static final String picProjectName = "DearCreative/"; //图片服务器的项目目录
    public static List<String> savePicture(MultipartFile[] files){
        List<String> names = null;
        //判断file数组不能为空并且长度大于0
        if(files != null && files.length > 0){
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            names = new ArrayList<>();
            //循环获取file数组中得文件
            for(int i = 0;i<files.length;i++){
                MultipartFile file = files[i];
                //保存文件
                String name = saveFile(file, date);
                if (StringUtils.isNotEmpty(name))
                    names.add(name);
            }
        }
        return names;
    }

    private static String saveFile(MultipartFile file, String date) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                String name = file.getOriginalFilename();
                // 文件保存路径
                String filePath = date + "/" + System.nanoTime() + name.substring(name.lastIndexOf("."));
                File fileTo = new File(rootPath + picProjectName +filePath);
                if (!fileTo.getParentFile().exists())
                    fileTo.mkdirs();
                // 转存文件
                file.transferTo(fileTo);
                //return filePath;
                return httpPath + picProjectName + filePath;//本地地址转化为http地址
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean deleteFile(String path){
        File file = new File(path.replace(httpPath, rootPath));//http地址转化为本地地址
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + path + "成功！");
                Logger.getLogger("删除单个文件" + path + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + path + "失败！");
                Logger.getLogger("删除单个文件" + path + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + path + "不存在！");
            Logger.getLogger("删除单个文件" + path + "不存在！");
            return false;
        }
    }
}
