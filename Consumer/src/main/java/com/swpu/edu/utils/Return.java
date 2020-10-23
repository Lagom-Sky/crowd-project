package com.swpu.edu.utils;

// 统一返回的结果统一工具类
import java.util.HashMap;
import java.util.Map;
public class Return {

    private Integer code;
    private String message;
    private Map<String, Object> data = new HashMap<>();
    private Return(){};

    // 成功时的返回值
    public static Return success(){
        Return rtn = new Return();
        rtn.setCode(200);
        rtn.setMessage("成功");
        return rtn;
    }
    // 失败时返回
    public static Return error(){
        Return rtn = new Return();
        rtn.setCode(201);
        rtn.setMessage("失败");
        return rtn;
    }

    public Integer getCode() {
        return code;
    }

    public Return setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Return setMessage(String message) {
        this.message = message;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Return setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}