package com.laowei.crowdfunding.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.laowei.crowdfunding.constant.CrowdConstant;
import com.laowei.crowdfunding.entity.Admin;
import com.laowei.crowdfunding.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @Author：Garrett
 * @Create：2020-07-14 19:08
 */
@Controller

public class AdminController {
    @Autowired
    private AdminService adminService;
    

    @RequestMapping("admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
    ){
        // 执行登录检查
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);
        // 将返回的admin对象存入Session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyword" , defaultValue = "") String keyword,
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "5") Integer pageSize,
            ModelMap modelMap
    ){
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);

        return "admin/admin-page";
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String removeAdmin(
                @PathVariable("adminId") Integer adminId,
                @PathVariable("pageNum") Integer pageNum,
                @PathVariable("keyword") String keyword
            ){
        adminService.remove(adminId);
        // 重定向回原操作页面
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String saveAdmin(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/{adminId}.html")
    public String updateAdmin(
            @PathVariable("adminId") Integer adminId,
            ModelMap modelMap
    ){
        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin",admin);

        return "admin/admin-edit";
    }

    @RequestMapping("/admin/update.html")
    public String updateAdmin(
            Admin admin,
            @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword)
    {
        adminService.updateAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

}
