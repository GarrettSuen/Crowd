package com.laowei.crowdfunding.service.api;

import com.github.pagehelper.PageInfo;
import com.laowei.crowdfunding.entity.Admin;

import java.util.List;

/**
 * @Author：Garrett
 * @Create：2020-07-14 16:22
 */
public interface AdminService {
    void saveAdmin(Admin admin);

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    PageInfo<Admin> getPageInfo(String keyword,Integer pageNum,Integer pageSize);

    void remove(Integer adminId);

    Admin getAdminById(Integer adminId);

    void updateAdmin(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleList);

    Admin getAdminByLoginaAct(String username);
}
