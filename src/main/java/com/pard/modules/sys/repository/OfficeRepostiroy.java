package com.pard.modules.sys.repository;

import com.pard.common.datatables.DataTablesRepository;
import com.pard.modules.sys.entity.Office;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
public interface OfficeRepostiroy extends DataTablesRepository<Office, String> {

    @Query(value = "select new Office(a.id,a.parent.id,a.name,a.parentIds,a.type) from Office a where a.delFlag = 0 order by a.parentIds, a.sort")
    List<Office> findAllWithTree();
}
