package com.moyu.brush.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MoyuBrushServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyuBrushServerApplication.class, args);
    }

}
