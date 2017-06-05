package com.pard.modules.sys.repository;

import com.pard.common.datatables.DataTablesRepository;
import com.pard.modules.sys.entity.Menu;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wawe on 17/5/15.
 */
@CacheConfig(cacheNames = "menus")
public interface MenuRepository extends DataTablesRepository<Menu, String> {
    @Cacheable
    @Query(value = "select m from Menu m where m.parent = null and m.delFlag = 0 order by m.sort")
    List<Menu> findAllByParentId();

    @Cacheable
    @Query(value = "select m from Menu m where m.parent.id = ?1 and m.delFlag = 0 order by m.sort")
    List<Menu> findAllByParentId(String pid);

    @Cacheable
    @Query(value = "select new Menu(id,parent.id,name,parentIds) from Menu m where m.delFlag = 0 and m.isShow='1' order by m.parentIds, m.sort")
    List<Menu> findAllWithTree();

    @Cacheable
    @Query(value = "select new Menu(id,parent.id,name,href,icon,sort, parentIds) from Menu m where m.delFlag = 0 and m.isShow ='1' order by m.parentIds, m.sort")
    List<Menu> findAllMenu();
}
