package com.laowei.crowdfunding.service.api;

import com.laowei.crowdfunding.entity.Menu;

import java.util.List;

/**
 * @Author：Garrett
 * @Create：2020-08-01 11:31
 */
public interface MenuService {
    List<Menu> getAll();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void removeMenuById(Integer id);
}
