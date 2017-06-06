package com.pard.modules.sys.service.impl;

import com.pard.common.constant.StringConstant;
import com.pard.common.service.impl.TreeServiceImpl;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Area;
import com.pard.modules.sys.repository.AreaRepository;
import com.pard.modules.sys.service.AreaService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
@CacheConfig(cacheNames = "areas")
@Component("areaService")
public class AreaServiceImpl extends TreeServiceImpl<Area, AreaRepository> implements AreaService {

    @Autowired
    @Override
    protected void setRepository(AreaRepository repository) {
        this.repository = repository;
    }

    @Override
    protected String getCacheName() {
        return "areas";
    }

    @Cacheable
    @Override
    public List<Area> findByParentId(String pid) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select new Area(id, parent.id, name, code, type, sort, remarks)")
                .append(" from Area a")
                .append(" where a.delFlag = :delFlag")
                .append(" and a.parent ")
                .append((StringUtils.isBlank(pid) || "0".equals(pid)) ? " is null" : " = :parent")
                .append(" order by a.sort");

        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("delFlag", Area.DEL_FLAG_NORMAL);
        if (StringUtils.isNotBlank(pid) && !"0".equals(pid)) {
            query.setParameter("parent", new Area(pid));
        }

        return query.getResultList();
    }

    @Cacheable
    @Override
    public List<Area> findAllWithTree() {
        return getRepository().findAllWithTree();
    }

    @CacheEvict(allEntries = true)
    @Override
    public void save(Area area) {
        if (area.getParent() != null) {
            if (area.getParent().getId().equals("0")) {
                area.setParent(null);
            } else {
                Area parent = getRepository().findOne(area.getParentId());
                if (parent != null) {
                    area.setParentIds(StringUtils.isNotBlank(parent.getParentIds()) ?
                            parent.getParentIds() + parent.getId() + ";" :
                            parent.getId() + ";"
                    );
                }
            }
        }
        area.setUpdateBy(UserUtils.getUser());
        super.save(area);
    }

    @CacheEvict(allEntries = true)
    @Override
    public void updateSort(List<Area> areas) {
        String hql = "update Area a set a.sort = :sort Where a.id = :id";
        Query query = entityManager.createQuery(hql);
        for (Area area : areas) {
            query.setParameter("sort", area.getSort());
            query.setParameter("id", area.getId());
            query.executeUpdate();
        }
        clearCache();
    }

    @CacheEvict(allEntries = true)
    @Override
    public void delete(String id) {
        super.delete(id);
    }
}
