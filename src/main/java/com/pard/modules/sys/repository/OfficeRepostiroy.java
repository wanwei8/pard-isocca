package com.pard.modules.sys.repository;

import com.pard.common.datatables.DataTablesRepository;
import com.pard.modules.sys.entity.Office;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
@CacheConfig(cacheNames = "offices")
public interface OfficeRepostiroy extends DataTablesRepository<Office, String> {
    @Cacheable
    @Query(value = "select new Office(a.id,a.parent.id,a.name,a.parentIds,a.type) from Office a where a.delFlag = 0 order by a.parentIds, a.sort")
    List<Office> findAllWithTree();

    @Cacheable
    @Query(value = "from Office a where a.delFlag = 0 and a.parent.id = ?1 order by a.sort")
    List<Office> findAllByParentId(String value);

    @Cacheable
    @Query(value = "from Office a where a.delFlag = 0 and a.parent = null order by a.sort")
    List<Office> findAllByParentId();
}
