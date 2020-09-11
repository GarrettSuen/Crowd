package com.laowei.crowdfunding.mvc.controller;

import com.laowei.crowdfunding.entity.Menu;
import com.laowei.crowdfunding.service.api.MenuService;
import com.laowei.crowdfunding.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author：Garrett
 * @Create：2020-08-01 11:33
 */
@RestController
public class MenuController {
    @Autowired
    private MenuService menuService;

    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTree(){
        // 1.查询出全部menu对象
        List<Menu> menuList = menuService.getAll();

        // 2.声明一个根节点
        Menu root = null;
        
        // 3.声明一个Map集合，用于存放Menu对象，为后续查找父节点做准备
        Map<Integer,Menu> menuMap = new HashMap<>();
        
        // 4.遍历menuList,存入menuMap中
        for (Menu menu : menuList) {
            // 4-1.获取id
            Integer id = menu.getId();
            
            // 4-2.存入menuMap中
            menuMap.put(id,menu);
        }
        
        // 5.再次遍历menuList,查找父节点、组装父子节点关系
        for (Menu menu : menuList) {
            // 5-1.获取pid
            Integer pid = menu.getPid();

            // 5-2.判断是否为根节点
            if (pid == null) {
                // 5-2-1.pid为null，当前对象即为根节点
                root = menu;

                // 5-2-2.进入下一次循环
                continue;
            }

            // 5-3.若当前对象的pid不为空，从menuMap中查找该对象的父节点
            Menu parentMenu = menuMap.get(pid);

            // 5-4.组装父子关系
            parentMenu.getChildren().add(menu);
        }
        // 5-5.将数据封装到ResultEntity中返回
        return ResultEntity.successWithData(root);
    }
    
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id){
        menuService.removeMenuById(id);
        return ResultEntity.successWithoutData();
    }
}
