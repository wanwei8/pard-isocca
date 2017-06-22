package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.TreeServiceImpl;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.repository.MenuRepository;
import com.pard.modules.sys.service.MenuService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/5/19.
 */
@CacheConfig(cacheNames = "menus")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Component("menuService")
public class MenuServiceImpl extends TreeServiceImpl<Menu, MenuRepository> implements MenuService {
    @Autowired
    @Override
    protected void setRepository(MenuRepository repository) {
        this.repository = repository;
    }

    @Cacheable
    @Override
    public Menu findOne(String id) {
        return getRepository().findOne(id);
    }

    @Transactional
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
        sbHql.append(" from Menu m")
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

    @Cacheable
    @Override
    public List<Menu> findByUser(User user) {
        return getRepository().findByUser(user.getId());
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void save(Menu menu) {
        if (menu.getParent() != null) {
            if (menu.getParent().getId().equals("0")) {
                menu.setParent(null);
            } else {
                Menu parent = findOne(menu.getParentId());
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

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void delete(String id) {
        super.delete(id);
    }
}
