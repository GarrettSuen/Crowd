package com.laowei.crowdfunding.mvc.config;

import com.google.gson.Gson;
import com.laowei.crowdfunding.constant.CrowdConstant;
import com.laowei.crowdfunding.exception.AccessForbiddenException;
import com.laowei.crowdfunding.exception.LoginAcctAlreadyInUseException;
import com.laowei.crowdfunding.exception.LoginAcctAlreadyInUseForUpdateException;
import com.laowei.crowdfunding.exception.LoginFailedException;
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
    /**
     * 公共异常请求解析方法，基于不同的请求（普通请求/Ajax请求）返回不同的处理
     * @param viewName 视图名称，基于普通请求的转发视图
     * @param exception 异常类型
     * @param request 请求对象，用于判断请求为普通请求或Ajax请求，以进行不同的处理
     * @param response 基于Ajax请求，响应相应JSON给浏览器
     * @return
     */
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
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 3-3.设置对应视图
        modelAndView.setViewName(viewName);
        // 4.返回模型视图
        return modelAndView;
    }

    /**
     * 登录异常方法
     * @param exception 自定义登录异常类
     * @param request 请求对象，用于传递给commonResolve方法判断当前请求是普通请求还是Ajax请求
     * @param response 响应对象，用于传递给commonResolve方法进行转发操作
     * @return 返回commonResolve的执行结果
     */
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginException(
            LoginFailedException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        String viewName = "login/admin-login";
        return commonResolve(viewName, exception, request, response);
    }
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveLoginException(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        String viewName = "error/system-error";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginException(
            LoginAcctAlreadyInUseException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        String viewName = "admin/admin-add";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginException(
            LoginAcctAlreadyInUseForUpdateException exception,
            HttpServletRequest request,
            HttpServletResponse response) {
        String viewName = "error/system-error";
        return commonResolve(viewName, exception, request, response);
    }
}
