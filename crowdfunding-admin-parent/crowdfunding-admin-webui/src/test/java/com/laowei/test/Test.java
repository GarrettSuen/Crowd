package com.laowei.test;

import com.laowei.crowdfunding.entity.Admin;
import com.laowei.crowdfunding.mapper.AdminMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author：Garrett
 * @Create：2020-07-21 21:11
 */
public class Test {
    @org.junit.Test
    public void test(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-persist-mybatis.xml");
        AdminMapper adminMapper = (AdminMapper) ctx.getBean("adminMapper");
        for (int i = 0; i < 100; i++) {
            String str = "100" + i ;
            String nm = "100" + i + "号";
            String email = "100" + i + "@qq.com";
            adminMapper.insert(new Admin(null,str,"FAE0B27C451C728867A567E8C1BB4E53",nm,email,null));
        }
    }

}
