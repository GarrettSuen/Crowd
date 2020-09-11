package com.laowei.crowdfunding.service.impl;

import com.laowei.crowdfunding.entity.Menu;
import com.laowei.crowdfunding.entity.MenuExample;
import com.laowei.crowdfunding.mapper.MenuMapper;
import com.laowei.crowdfunding.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

/**
 * @Author：Garrett
 * @Create：2020-08-01 11:32
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }

    @Override
    public void saveMenu(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        // 选择有选择的更新，避免pid被置空
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void removeMenuById(Integer id) {
        menuMapper.deleteByPrimaryKey(id);
    }
}
