package com.laowei.crowdfunding.mvc.controller;

import com.laowei.crowdfunding.constant.CrowdConstant;
import com.laowei.crowdfunding.entity.Auth;
import com.laowei.crowdfunding.entity.Role;
import com.laowei.crowdfunding.service.api.AdminService;
import com.laowei.crowdfunding.service.api.AuthService;
import com.laowei.crowdfunding.service.api.RoleService;
import com.laowei.crowdfunding.util.ResultEntity;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Author：Garrett
 * @Create：2020-08-12 16:08
 */
@Controller
public class AssignController {
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;
    
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId, 
            ModelMap modelMap
    ){
        // 1.查询已分配的Role
        List<Role> assignRoleList = roleService.getAssignRole(adminId);
        
        // 2.查询未分配的Role
        List<Role> unAssignRoleList = roleService.getUnAssignRole(adminId);
        
        // 3.存入模型中
        modelMap.addAttribute("assignRoleList",assignRoleList);
        modelMap.addAttribute("unAssignRoleList",unAssignRoleList);
        return "assign/assign-role";
    }
    
    @RequestMapping("/assign/do/role/assgin.html")
    public String saveAdminRoleRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "roleIdList",required = false) List<Integer> roleList
    ){
        adminService.saveAdminRoleRelationship(adminId,roleList);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
       
    }

    @ResponseBody
    @RequestMapping("/auth/get/auth.json")
    public ResultEntity<List<Auth>> getAllAuth(){
        // 1.查询出所有权限对象
        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }
    
    @ResponseBody
    @RequestMapping("/auth/get/assign/authId.json")
    public ResultEntity<List<Integer>> getAssignAuthByRoleId(@RequestParam("roleId") Integer roleId){
        List<Integer> authIds = authService.getAssignAuthByRoleId(roleId);
        return ResultEntity.successWithData(authIds);
    }
    
    @ResponseBody
    @RequestMapping("/assign/do/assign/auth.json")
    public ResultEntity<List<Integer>> saveRoleAuthRelationship(@RequestBody Map<String,List<Integer>> map){
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }
}
