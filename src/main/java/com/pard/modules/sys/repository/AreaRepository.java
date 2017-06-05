package com.pard.modules.sys.repository;

import com.pard.common.datatables.DataTablesRepository;
import com.pard.modules.sys.entity.Area;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by wawe on 17/4/27.
 */
@CacheConfig(cacheNames = "areas")
public interface AreaRepository extends DataTablesRepository<Area, String> {
    @Cacheable
    @Query(value = "select new Area(id,parent.id,name, parentIds) from Area a where a.delFlag = 0 order by a.parentIds, a.sort")
    List<Area> findAllWithTree();

    @Cacheable
    @Query(value = "from Area a where a.delFlag = 0 and a.parent.id = ?1 order by a.sort")
    List<Area> findAllByParentId(String value);

    @Cacheable
    @Query(value = "from Area a where a.delFlag = 0 and a.parent = null order by a.sort")
    List<Area> findAllByParentId();
}
