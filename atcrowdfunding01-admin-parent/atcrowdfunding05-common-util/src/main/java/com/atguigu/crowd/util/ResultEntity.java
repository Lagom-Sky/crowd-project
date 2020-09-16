package com.atguigu.crowd.util;

import com.sun.net.httpserver.Authenticator;

/**
 * 统一整个Ajax请求返回的结果（为来也可以y用于分布式架构各个模块间的返回统一类型
 * @param <T>
 */
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    //用来封装当前请求时处理的结果是成功还是失败
    private String result ;

    //请求处理失败时返回的错误消息
    private String message;

    //返回给浏览器的数据
    private T Data;

    /**
     * 请求处理成功时且不需要返回数据时使用的工具方法
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithoutData(){

        return  new ResultEntity<Type>(SUCCESS,null,null);
    }

    /**
     * 请求处理成功且需要返回数据的时候使用的工具方法
     * @param Data 要返回的数据一般是查询方法
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type Data){

        return  new ResultEntity<Type>(SUCCESS,null,Data);
    }

    /**
     * 请求处理失败以后调用的方法
     * @param message 失败的错误消息
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> failed(String message){

        return  new ResultEntity<Type>(FAILED,message,null);
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        Data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", Data=" + Data +
                '}';
    }
}
