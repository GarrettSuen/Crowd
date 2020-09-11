package com.laowei.crowdfunding.service.impl;

import com.laowei.crowdfunding.entity.Auth;
import com.laowei.crowdfunding.entity.AuthExample;
import com.laowei.crowdfunding.mapper.AuthMapper;
import com.laowei.crowdfunding.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author：Garrett
 * @Create：2020-08-14 17:02
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public List<Auth> getAll() {
        return authMapper.selectByExample(new AuthExample() );
    }

    @Override
    public List<Integer> getAssignAuthByRoleId(Integer roleId) {
        return authMapper.getAssignAuthByRoleId(roleId);
    }

    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        // 1.取出数据
        List<Integer> roleIds = map.get("roleId");
        List<Integer> authIds = map.get("authIds");
        Integer roleId = roleIds.get(0);
        // 2.删除旧的关联关系
        authMapper.removeOriginalRelationship(roleId);
        if(authIds.size() > 0 && authIds != null){
            // 3.建立新的角色权限关联关系
            authMapper.insertNewRelationship(roleId,authIds);
        }
    }

    @Override
    public List<String> getAssignAuthNameByRoleId(Integer adminId) {
        return authMapper.selectAssignAuthNameByRoleId(adminId);
    }
}
