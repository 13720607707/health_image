package com.health;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class})
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDubbo
public class HealthMobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthMobileApplication.class, args);
    }

}
