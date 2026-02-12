package com.yupi.yupao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类（Spring Boot 入口）。
 *
 * @author Ethan
 */
@SpringBootApplication
@MapperScan("com.yupi.yupao.mapper")
@EnableScheduling
public class MyApplication {

    /**
     * 应用启动入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
