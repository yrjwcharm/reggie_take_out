package com.itheima.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;
public class UploadFileTest {
    @Test
    public void test1 (){
            String fileName ="22222.jpg";
           String suffix= fileName.substring(fileName.lastIndexOf("."));
          System.out.println(suffix);
    }
}
