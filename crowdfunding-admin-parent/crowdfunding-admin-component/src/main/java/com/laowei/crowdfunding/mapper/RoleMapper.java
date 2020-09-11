package com.laowei.crowdfunding.mapper;

import com.laowei.crowdfunding.entity.Role;
import com.laowei.crowdfunding.entity.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：Garrett
 * @Create：2020-07-28 12:25
 */
public interface RoleMapper {
    int countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectRoleByKeyword(String keyword);

    List<Role> selectAssignRole(Integer adminId);

    List<Role> selectUnAssignRole(Integer adminId);
}
