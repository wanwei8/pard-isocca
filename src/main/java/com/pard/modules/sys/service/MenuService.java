package com.pard.modules.sys.service;

import com.pard.common.security.CustomUserDetails;
import com.pard.common.service.BaseService;
import com.pard.common.service.TreeService;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by wawe on 17/5/19.
 */
public interface MenuService extends TreeService<Menu> {
    void updateSort(List<Menu> menus);

    List<Menu> findByParentId(String pid);

    List<Menu> findAllMenu();

    List<Menu> findByUser(User user);
}
