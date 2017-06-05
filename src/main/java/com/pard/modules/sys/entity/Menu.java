package com.pard.modules.sys.entity;

import com.pard.common.persistence.TreeEntity;
import com.pard.common.utils.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by wawe on 17/5/15.
 */
@Entity
@Table(name = "sys_menu", indexes = {
        @Index(columnList = "parent_id", name = "IDX_SYS_MENU_PARENT"),
})
public class Menu extends TreeEntity<Menu> {
    private static final long serialVersionUID = 1L;
    /**
     * 链接
     */
    @Column(length = 2000)
    private String href;
    /**
     * 图标
     */
    @Column(length = 100)
    private String icon;

    /**
     * 是否在菜单中显示
     */
    @Column(length = 1, nullable = false)
    private String isShow;

    /**
     * 权限标识
     */
    @Column(length = 200)
    private String permission;

    @Override
    public Menu getParent() {
        return parent;
    }

    @Override
    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public Menu() {
        super();
        this.isShow = YES;
    }

    public Menu(String id) {
        super(id);
    }

    public Menu(String id, String parentId, String name, String parentIds) {
        this(id);
        if (StringUtils.isNotBlank(parentId)) {
            this.parent = new Menu(parentId);
        }
        this.name = name;
        this.parentIds = parentIds;
    }

    public Menu(String id, String pid, String name, String href, String icon, Integer sort, String parentIds) {
        this(id, pid, name, parentIds);
        this.href = href;
        this.icon = icon;
        this.sort = sort;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
