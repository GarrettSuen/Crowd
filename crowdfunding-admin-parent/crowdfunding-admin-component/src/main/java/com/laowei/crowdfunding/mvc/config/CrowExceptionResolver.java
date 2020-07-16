package com.laowei.crowdfunding.mvc.config;

import com.google.gson.Gson;
import com.laowei.crowdfunding.util.CrowdUtil;
import com.laowei.crowdfunding.util.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author：Garrett
 * @Create：2020-07-16 16:09
 */
@ControllerAdvice
public class CrowExceptionResolver {
    private ModelAndView commonResolve(String viewName, Exception exception, HttpServletRequest request, HttpServletResponse response) {
        // 1.判断当前类型
        boolean judgeRequest = CrowdUtil.judgeRequestType(request);
        // 2.如果请求为Ajax请求
        if (judgeRequest) {
            // 2-1.创建ResultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            // 2-2.创建GSON对象
            Gson gson = new Gson();
            // 2-3.将ResultEntity对象转换为JSON字符串
            String json = gson.toJson(resultEntity);
            // 2-4.将JSON字符串响应给浏览器
            try {
                response.getWriter().write(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        // 3.如果为普通请求
        // 3-1.创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        // 3-2.将Exception对象存入模型中
        modelAndView.addObject("exception", exception);
        // 3-3.设置对应视图
        modelAndView.setViewName(viewName);
        // 4.返回模型视图
        return modelAndView;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveMathException(
            ArithmeticException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }
}
