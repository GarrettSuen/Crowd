package com.laowei.crowdfunding.service.api;

import com.laowei.crowdfunding.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * @Author：Garrett
 * @Create：2020-08-14 17:02
 */
public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAssignAuthByRoleId(Integer roleId);

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);

    List<String> getAssignAuthNameByRoleId(Integer adminId);
}
