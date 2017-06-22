package com.pard.modules.sys.repository;

import com.pard.common.datatables.DataTablesRepository;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wawe on 17/5/15.
 */
public interface MenuRepository extends DataTablesRepository<Menu, String> {
    @Query(value = "select m from Menu m where m.parent = null and m.delFlag = 0 order by m.sort")
    List<Menu> findAllByParentId();

    @Query(value = "select m from Menu m where m.parent.id = ?1 and m.delFlag = 0 order by m.sort")
    List<Menu> findAllByParentId(String pid);

    @Query(value = "select m from Menu m where m.delFlag = 0 and m.isShow='1' order by m.parentIds, m.sort")
    List<Menu> findAllWithTree();


    @Query(value = "select m from Menu m where m.delFlag = '0' order by m.parentIds, m.sort")
    List<Menu> findAllMenu();

    @Query(value = "select distinct m from Menu m join m.roleList r join r.userList u where m.delFlag = '0' and u.id = :userid order by m.id")
    List<Menu> findByUser(@Param("userid") String userid);
}
