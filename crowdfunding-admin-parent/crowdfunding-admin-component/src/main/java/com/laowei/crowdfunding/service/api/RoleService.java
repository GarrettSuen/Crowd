package com.laowei.crowdfunding.service.api;

import com.github.pagehelper.PageInfo;
import com.laowei.crowdfunding.entity.Role;

import java.util.List;

/**
 * @Author：Garrett
 * @Create：2020-07-27 19:25
 */
public interface RoleService {
    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    void saveRole(Role role);

    void updateRole(Role role);

    void deleteRole(List<Integer> roleIds);

    List<Role> getAssignRole(Integer adminId);

    List<Role> getUnAssignRole(Integer adminId);
}
