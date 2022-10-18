package com.moyu.brush.server.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

@Configuration
@MapperScan("com.moyu.brush.server.mapper")
public class MybatisPlusConfig {

    @Bean
    MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Component
    public static class MybatisPlusMetaHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            this.setFieldValByName("createTime", new Date(), metaObject);
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }
    }

}
