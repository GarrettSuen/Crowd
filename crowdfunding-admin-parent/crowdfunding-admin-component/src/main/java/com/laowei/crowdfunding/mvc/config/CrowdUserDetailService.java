package com.laowei.crowdfunding.mvc.config;

import com.laowei.crowdfunding.entity.Admin;
import com.laowei.crowdfunding.entity.Role;
import com.laowei.crowdfunding.service.api.AdminService;
import com.laowei.crowdfunding.service.api.AuthService;
import com.laowei.crowdfunding.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：Garrett
 * @Create：2020-08-24 21:43
 */
@Component
public class CrowdUserDetailService implements UserDetailsService {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据username查询出Admin对象
        Admin admin = adminService.getAdminByLoginaAct(username);
        // 2.获取adminid
        Integer adminId = admin.getId();
        // 3.根据adminId查询出角色信息
        List<Role> assignRole = roleService.getAssignRole(adminId);
        // 4.根据adminId查询出权限信息
        List<String> assignAuthName = authService.getAssignAuthNameByRoleId(adminId);
        // 5.创建集合用于存储GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 6.遍历assignRole集合存入角色信息
        for (Role role : assignRole) {
            String roleName = "ROLE_" + role.getRoleName();
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleName);
            authorities.add(grantedAuthority);
        }
        // 7.遍历assignAuthName 存入权限信息
        for (String authName : assignAuthName) {
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authName);
            authorities.add(grantedAuthority);
        }
        // 8.封装SecurityUser对象
        SecurityUser securityUser = new SecurityUser(admin, authorities);
        return securityUser;
    }
}
