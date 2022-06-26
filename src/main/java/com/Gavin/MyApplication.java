package com.Gavin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Gavin
 * @description:
 * @className: MyApplication
 * @date: 2022/5/13 20:36
 * @version:0.1
 * @since: jdk14.0
 */
@SpringBootApplication
@ServletComponentScan       //扫描web组件注解 如过滤器
@EnableTransactionManagement    //开启事务注解
@EnableCaching
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class,args);
    }
}
