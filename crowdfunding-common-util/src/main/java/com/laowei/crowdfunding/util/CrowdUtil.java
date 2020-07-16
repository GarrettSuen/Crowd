package com.laowei.crowdfunding.util;


import javax.servlet.http.HttpServletRequest;

/**
 * @Author：Garrett
 * @Create：2020-07-16 13:47
 */
public class CrowdUtil {
    /**
     * 判断当前请求是否为Ajax请求
     * @param request 请求对象
     * @return true / false
     *      true ： Ajax请求
     *      false ： 普通请求
     */
    public static boolean judgeRequestType(HttpServletRequest request){
        // 获取到相应的请求头参数
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");
        // 基于请求头参数判断请求类型并返回
        return (acceptHeader != null && acceptHeader.contains("application/json")) || (xRequestHeader !=null && xRequestHeader.contains("XMLHttpRequest"));
    }
}
