package com.atguigu.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence arg0) {
        return privateEncode(arg0);
    }

    @Override
    public boolean matches(CharSequence arg0, String arg1)
    {
        // 先对明文密码进行加密
        String rawPassword = privateEncode(arg0);

        return arg1.equals(rawPassword);
    }

    private String privateEncode(CharSequence arg0){

        // 1.创建MessageDigest对象
        try {
            String algorithm = "MD5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            byte[] inputs = ((String)arg0).getBytes();

            // 2.实现加密
            byte[] outputs = messageDigest.digest(inputs);

            // 3.转换为十六进制数对应的字符
            String encode = new BigInteger(1, outputs).toString(16).toUpperCase();

            return encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return arg0.toString();
    }

}