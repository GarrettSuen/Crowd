package com.laowei.crowdfunding.util;

/**
 * 统一返回结果工具类
 * 目前用于处理Ajax请求时返回的统一类型数据
 * 未来引入分布式架构后也可作用于各个模块间调用时返回类型统一处理
 * @param <T>
 */

public class ResultEntity<T> {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    // 封装当前请求的结果
    private String result;

    // 封装当前请求的消息
    private String message;

    //封装当前请求的数据
    private T data;

    /**
     * 请求处理成功且不需要返回数据时使用的方法
     * @return
     */
    public static <Type> ResultEntity<Type> successWithoutData(){
        return new ResultEntity<Type>(SUCCESS,null,null);
    }

    /**
     * 请求处理成功且需要返回数据的方法
     * @param data 返回的类型数据
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type data){
        return new ResultEntity<Type>(SUCCESS,null,data);
    }

    /**
     * 请求失败时返回失败原因的方法
     * @param message 失败消息
     * @return
     */
    public static <Type> ResultEntity<Type> failed(String message){
        return new ResultEntity<Type>(FAILED,message,null);
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
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
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public ResultEntity() {}
}
