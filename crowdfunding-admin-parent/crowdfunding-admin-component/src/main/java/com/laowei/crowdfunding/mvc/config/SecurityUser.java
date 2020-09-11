package com.laowei.crowdfunding.mvc.config;

import com.laowei.crowdfunding.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * Userdetails接口实现类User的扩展类
 * 实现类User只有username和password，为了得到完整的数据使用SecurityUser继承User后进行扩展
 * @Author：Garrett
 * @Create：2020-08-24 21:08
 */
public class SecurityUser extends User {
    private static final long serialVersionUID = -7836419788015673418L;
    // 原生Admin对象
    private Admin originalAdmin;
    
    public SecurityUser(
            // 传入的原生Admin对象
            Admin originalAdmin , 
            // 创建角色和权限集合
            List<GrantedAuthority> authorities
    ){
        // 调用父类构造器
        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPswd(),authorities);
        // this.originalAdmin对象进行赋值
        this.originalAdmin = originalAdmin;
        // 擦除密码凭证
        this.originalAdmin.setUserPswd(null);
    }
    
    // 获取原生Admin对象方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }

}
