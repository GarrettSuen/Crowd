package com.laowei.crowdfunding.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security配置类
 * @Author：Garrett
 * @Create：2020-08-24 13:59
 */
@Configuration       // 申明为配置类
@EnableWebSecurity  // 开启web环境权限控制功能
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启全局方法权限验证,并设置prePostEnable = true
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    
   @Autowired
   private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 基于数据库数据进行验证
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()                                                // 请求授权
                .antMatchers("/admin/to/login/page.html")              // 针对后台登录页面授权
                .permitAll()                                                        // 无条件放行
                // 放行静态资源
                .antMatchers("/bootstrap/**")
                .permitAll()
                .antMatchers("/crowd-js/**")
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/layer/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()
                .antMatchers("/admin/get/page.html")                   // 为查询用户分页设定权限
                //.hasRole("经理")                                                    // 必须具备经理角色才能访问
                .access("hasRole('经理') OR hasAnyAuthority('user:get')")
                .anyRequest()                                                       // 其他请求
                .authenticated()                                                    // 需要登录后才能访问
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        request.setAttribute("exception",new Exception("无法访问！"));
                        request.getRequestDispatcher("/WEB-INF/views/error/system-error.jsp").forward(request,response);
                    }
                })
                .and()
                // 禁用跨站伪造功能
                .csrf()
                .disable()
                .formLogin()                                                        // 开启表单登录
                .loginPage("/admin/to/login/page.html")                             // 登录页面
                .loginProcessingUrl("/security/do/login.html")                      // 处理请求地址
                .defaultSuccessUrl("/admin/to/main/page.html")                      // 登录成功后前往的页面
                .usernameParameter("loginAcct")                                     // 登录账号请求名称
                .passwordParameter("userPswd")                                      // 登录密码请求名称
                .and()
                .logout()                                                           // 开启退出登录功能
                .logoutUrl("/security/do/logout.html")                              // 处理退出登录地址
                .logoutSuccessUrl("/admin/to/login/page.html")                      // 退出后的页面地址
        ;
    }
}
