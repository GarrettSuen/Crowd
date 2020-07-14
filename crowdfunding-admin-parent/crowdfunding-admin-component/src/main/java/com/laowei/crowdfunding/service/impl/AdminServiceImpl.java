package com.laowei.crowdfunding.service.impl;

import com.laowei.crowdfunding.entity.Admin;
import com.laowei.crowdfunding.mapper.AdminMapper;
import com.laowei.crowdfunding.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author：Garrett
 * @Create：2020-07-14 16:23
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void saveAdmin(Admin admin) {
        adminMapper.insert(admin);
    }
}
