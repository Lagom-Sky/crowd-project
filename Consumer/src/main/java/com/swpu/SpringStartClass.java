package com.swpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ImportResource("classpath:transaction.xml")
public class SpringStartClass {
    public static void main(String[] args) {
        SpringApplication.run(SpringStartClass.class);
    }
}
