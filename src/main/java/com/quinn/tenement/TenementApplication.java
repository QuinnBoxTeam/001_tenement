package com.quinn.tenement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */

@SpringBootApplication
@MapperScan("com.quinn.tenement.mapper")
public class TenementApplication implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(TenementApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        String url = "http://localhost:8080/";

        if (os.contains("win")) {
            Runtime.getRuntime().exec("cmd /c start " + url);
        } else if (os.contains("mac")) {
            Runtime.getRuntime().exec("open " + url);
        }
    }
}