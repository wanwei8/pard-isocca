package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.TreeServiceImpl;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.repository.MenuRepository;
import com.pard.modules.sys.service.MenuService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/5/19.
 */
@CacheConfig(cacheNames = "menus")
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

    @CacheEvict(allEntries = true)
    @Override
    public void updateSort(List<Menu> menus) {
        Query query = entityManager.createQuery("update Menu m set m.sort = :sort Where m.id = :id");
        for (Menu menu : menus) {
            query.setParameter("sort", menu.getSort());
            query.setParameter("id", menu.getId());
            query.executeUpdate();
        }
    }

    @Cacheable
    @Override
    public List<Menu> findByParentId(String pid) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select new Menu(id, parent.id, name, href, isShow, sort, permission, icon)")
                .append(" from Menu m")
                .append(" where m.delFlag = :delFlag")
                .append(" and m.parent ")
                .append((StringUtils.isBlank(pid) || "0".equals(pid)) ? " is null" : " = :parent")
                .append(" order by m.sort");

        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("delFlag", Menu.DEL_FLAG_NORMAL);
        if (StringUtils.isNotBlank(pid) && !"0".equals(pid)) {
            query.setParameter("parent", new Menu(pid));
        }

        return query.getResultList();
    }

    @Cacheable
    @Override
    public List<Menu> findAllMenu() {
        return getRepository().findAllMenu();
    }

    @CacheEvict(allEntries = true)
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

    @CacheEvict(allEntries = true)
    @Override
    public void delete(String id) {
        super.delete(id);
    }
}
