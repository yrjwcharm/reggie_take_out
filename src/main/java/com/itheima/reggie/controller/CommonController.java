package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传与下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir");
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file是一个临时文件,需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info("file:{}", file);
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
//        file.transferTo(new File("/Users/yanruifeng/Desktop/my-react-app/1.jpg"));
        //创建一个目录对象
        String originFilename = file.getOriginalFilename();
        String suffix=originFilename.substring(originFilename.lastIndexOf("."));
        String newFileName=  UUID.randomUUID()+suffix;
        file.transferTo(new File(UPLOAD_DIRECTORY+basePath+newFileName));
        return R.success(newFileName);
    }
    @GetMapping("/download")
    public void downloadFile(String name, HttpServletResponse response){
        try {
            //通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(UPLOAD_DIRECTORY+basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len;
            byte[] bytes = new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                    outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//前端图片展示 文件上传后 后端直接返回阿里云OSS图片地址，后端自己拼接访问图片，前端请求文件下载的响应输出流，