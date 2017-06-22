package com.pard.modules.sys.repository;

import com.pard.common.datatables.DataTablesRepository;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wawe on 17/6/12.
 */
public interface RoleRepository extends DataTablesRepository<Role, String> {

    @Query("select r from Role r where r.office.id = :oid or r.office.parentIds like :pid")
    List<Role> findByOffice(@Param("oid") String oid, @Param("pid") String pid);
}
