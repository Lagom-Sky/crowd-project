package com.atguigu.crowd.config;


import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "short.message")
public class ShortMessageProperties {
    
    // Api身份验证码
    String appCode;

    // 请求地址
    String host;

    // 请求方式
    String method;

    // 请求路径
    String path;

    // 签名
    String sign;

    // 模板
    String skin;
}
