package com.laowei.crowdfunding.mvc.controller;

import com.github.pagehelper.PageInfo;
import com.laowei.crowdfunding.entity.Role;
import com.laowei.crowdfunding.service.api.RoleService;
import com.laowei.crowdfunding.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author：Garrett
 * @Create：2020-07-27 16:49
 */
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('部长')")
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum" , defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize" , defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword" , defaultValue = "") String keyword
    ){
        // 1.执行Service方法查询得到pageInfo
        // tips:出现异常会被异常映射捕获并处理
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        // 2.使用ResultEntity封装数据和信息，并返回给页面
        return ResultEntity.successWithData(pageInfo);
    }

    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role){
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/remove/id/array.json")
    public ResultEntity<String> deleteRoleByIds(@RequestBody List<Integer> roleidArray){
        roleService.deleteRole(roleidArray);
        return ResultEntity.successWithoutData();
    }
}
