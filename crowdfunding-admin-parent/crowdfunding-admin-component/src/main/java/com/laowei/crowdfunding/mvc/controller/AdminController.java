package com.laowei.crowdfunding.mvc.controller;

import com.laowei.crowdfunding.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author：Garrett
 * @Create：2020-07-14 19:08
 */
@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
}
