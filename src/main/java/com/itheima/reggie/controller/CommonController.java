package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        log.info("文件上传路径{}",UPLOAD_DIRECTORY);
//        file.transferTo(new File("/Users/yanruifeng/Desktop/my-react-app/1.jpg"));
        file.transferTo(new File(UPLOAD_DIRECTORY+basePath+"hello.jpg"));
        return null;
    }
}
