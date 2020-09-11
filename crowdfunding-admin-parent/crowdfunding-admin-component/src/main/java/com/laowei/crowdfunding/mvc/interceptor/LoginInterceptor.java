package com.laowei.crowdfunding.mvc.interceptor;

import com.laowei.crowdfunding.constant.CrowdConstant;
import com.laowei.crowdfunding.entity.Admin;
import com.laowei.crowdfunding.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器：用于保护资源不被非法访问
 * @Author：Garrett
 * @Create：2020-07-20 12:56
 */
@Deprecated
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         // 1.通过request对象获取session对象
        HttpSession session = request.getSession();
        // 2.尝试通过session.getAttribute获取到Admin对象
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        // 3.判断对象是否为空
        if (admin == null){
            // 3-1.抛出异常,必须先进行登录后才能访问
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIGEN);
        }
        // 4.否则放行
        return true;
    }
}
