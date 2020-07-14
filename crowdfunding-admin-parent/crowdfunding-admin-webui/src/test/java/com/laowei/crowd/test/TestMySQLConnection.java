package com.laowei.crowd.test;

import com.laowei.crowdfunding.entity.Admin;
import com.laowei.crowdfunding.mapper.AdminMapper;
import com.laowei.crowdfunding.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author：Garrett
 * @Create：2020-07-14 11:17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:Spring/spring-persist-*.xml"})
public class TestMySQLConnection {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminService adminService;
    @Test
    public void testAdminServiceMethod(){
        adminService.saveAdmin(new Admin(null,"1003","666","admin2","admin2@163.com",null));
    }
    @Test
    public void TestConnection(){
        try {
            Connection connection = dataSource.getConnection();
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestFisrtSelect(){
        Admin admin = adminMapper.selectByPrimaryKey(1);
        System.out.println(admin);
    }

    @Test
    public void testLog(){
        Logger logger = LoggerFactory.getLogger(TestMySQLConnection.class);
        logger.debug("debug level!!!");
        logger.debug("debug level!!!");
        logger.debug("debug level!!!");

        logger.info("info level!!!");
        logger.info("info level!!!");
        logger.info("info level!!!");

        logger.warn("warn level!!!");
        logger.warn("warn level!!!");
        logger.warn("warn level!!!");

        logger.error("error level!!!");
        logger.error("error level!!!");
        logger.error("error level!!!");
    }
}
