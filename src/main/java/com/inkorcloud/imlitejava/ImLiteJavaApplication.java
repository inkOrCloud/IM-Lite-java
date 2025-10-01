package com.inkorcloud.imlitejava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@EnableRetry
@Validated
@MapperScan("com.inkorcloud.imlitejava.dao.mapper")
public class ImLiteJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImLiteJavaApplication.class, args);
    }

}
