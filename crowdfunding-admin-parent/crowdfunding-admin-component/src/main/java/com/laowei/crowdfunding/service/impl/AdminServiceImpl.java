package com.laowei.crowdfunding.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.laowei.crowdfunding.constant.CrowdConstant;
import com.laowei.crowdfunding.entity.Admin;
import com.laowei.crowdfunding.entity.AdminExample;
import com.laowei.crowdfunding.exception.LoginAcctAlreadyInUseException;
import com.laowei.crowdfunding.exception.LoginAcctAlreadyInUseForUpdateException;
import com.laowei.crowdfunding.exception.LoginFailedException;
import com.laowei.crowdfunding.mapper.AdminMapper;
import com.laowei.crowdfunding.service.api.AdminService;
import com.laowei.crowdfunding.util.CrowdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author：Garrett
 * @Create：2020-07-14 16:23
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1.通过PageHelper开启分页功能
        PageHelper.startPage(pageNum,pageSize);
        // 2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);
        // 3.封装为PageInfo对象
        PageInfo<Admin> pageInfo = new PageInfo<>(list);
        // 4.返回pageInfo
        return pageInfo;
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        Admin admin = adminMapper.selectByPrimaryKey(adminId);
        return admin;
    }

    @Override
    public void updateAdmin(Admin admin) {
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdmin(Admin admin) {
        String unencrypted = admin.getUserPswd();
        String encoded = passwordEncoder.encode(unencrypted);
        admin.setUserPswd(encoded);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 根据账号查询用户
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> list = adminMapper.selectByExample(adminExample);

        // 针对异常情况进行处理
        if (list == null || list.size() == 0){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (list.size() > 1){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_ACCT_NOT_UNIQUE);
        }

        Admin admin = list.get(0);
        if (admin == null){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 进行密码比较
        String pswdDB = admin.getUserPswd().toUpperCase();
        // 将表单的明文密码进行加密
        String pswdForm = CrowdUtil.md5(userPswd);

        if (!Objects.equals(pswdDB,pswdForm)){
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        return admin;
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleList) {
        // 1.根据adminId删除原先的关联关系
        adminMapper.deleteRelationship(adminId);
        // 2.判断roleList是否非空
        if (roleList != null){
            adminMapper.insertNewRelationship(adminId,roleList);
        }
    }

    @Override
    public Admin getAdminByLoginaAct(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        Admin admin = list.get(0);
        return admin;
    }

}
