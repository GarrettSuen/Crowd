package com.laowei.crowdfunding.util;


import com.laowei.crowdfunding.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 众筹网通用工具类
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

    /**
     * MD5加密方法
     * @param source 加密的明文
     * @return encoded 密文
     */
    public static String md5(String source){
        // 1.判断source是否有效
        if (source == null || source.length() == 0){
            // 1-1.如果source是非法的字符,抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        try {
            // 2.获取MessageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 3.获取明文字符串对应的字节数组
            byte[] input = source.getBytes();
            // 4.执行加密
            byte[] output = messageDigest.digest(input);
            // 5.创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);
            // 6.指定bigInteger对象按照16进制转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();
            // 将密文返回
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
