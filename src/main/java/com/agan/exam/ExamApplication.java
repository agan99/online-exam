package com.agan.exam;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@MapperScan("com.agan.exam.dao")
@SpringBootApplication
public class ExamApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ExamApplication.class)
        .web(WebApplicationType.SERVLET)
        .run(args);
    }
}
