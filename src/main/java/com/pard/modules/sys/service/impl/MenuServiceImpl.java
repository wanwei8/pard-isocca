package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.TreeServiceImpl;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.repository.MenuRepository;
import com.pard.modules.sys.service.MenuService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/5/19.
 */
@Component("menuService")
public class MenuServiceImpl extends TreeServiceImpl<Menu, MenuRepository> implements MenuService {
    @Autowired
    @Override
    protected void setRepository(MenuRepository repository) {
        this.repository = repository;
    }

    @Override
    protected String getCacheName() {
        return "menus";
    }

    @Override
    public void updateSort(List<Menu> menus) {
        Query query = entityManager.createQuery("update Menu m set m.sort = :sort Where m.id = :id");
        for (Menu menu : menus) {
            query.setParameter("sort", menu.getSort());
            query.setParameter("id", menu.getId());
            query.executeUpdate();
        }
        clearCache();
    }

    @Override
    public List<Menu> findByParentId(String pid) {
        if (StringUtils.isBlank(pid) || "0".equals(pid)) {
            return getRepository().findAllByParentId();
        }
        return getRepository().findAllByParentId(pid);
    }

    @Override
    public List<Menu> findWithTree() {
        return getRepository().findAllWithTree();
    }

    @Override
    public void save(Menu menu) {
        if (menu.getParent() != null) {
            if (menu.getParent().getId().equals("0")) {
                menu.setParent(null);
            } else {
                Menu parent = getRepository().findOne(menu.getParentId());
                if (parent != null) {
                    menu.setParentIds(StringUtils.isNotBlank(parent.getParentIds()) ?
                            parent.getParentIds() + parent.getId() + ";" :
                            parent.getId() + ";"
                    );
                }
            }
        }
        super.save(menu);
    }

    @Override
    public List<Menu> findAllMenu() {
        return getRepository().findAllMenu();
    }
}
