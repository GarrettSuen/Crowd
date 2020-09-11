package com.laowei.crowdfunding.entity;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    // 主键
    private Integer id;

    // 父节点id
    private Integer pid;

    // 节点名称
    private String name;

    // 节点地址
    private String url;

    // 节点图标
    private String icon;

    // 子节点集合
    private List<Menu> children = new ArrayList<>();

    // 默认展开节点
    private boolean isOpen = true;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Menu() {}

    public Menu(Integer id, Integer pid, String name, String url, String icon, List<Menu> children, boolean isOpen) {
        this.id = id;
        this.pid = pid;
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.children = children;
        this.isOpen = isOpen;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                ", isOpen=" + isOpen +
                '}';
    }
}