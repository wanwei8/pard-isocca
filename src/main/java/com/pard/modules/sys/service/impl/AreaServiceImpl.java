package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.TreeServiceImpl;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Area;
import com.pard.modules.sys.repository.AreaRepository;
import com.pard.modules.sys.service.AreaService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
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

    @Override
    public List<Area> findByParentId(String pid) {
        if (StringUtils.isBlank(pid) || "0".equals(pid)) {
            return getRepository().findAllByParentId();
        }
        return getRepository().findAllByParentId(pid);
    }

    @Override
    public List<Area> findAllWithTree() {
        return getRepository().findAllWithTree();
    }

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

    @Override
    public void updateSort(List<Area> areas) {
        Query query = entityManager.createQuery("update Area m set m.sort = :sort Where m.id = :id");
        for (Area area : areas) {
            query.setParameter("sort", area.getSort());
            query.setParameter("id", area.getId());
            query.executeUpdate();
        }
        clearCache();
    }
}
