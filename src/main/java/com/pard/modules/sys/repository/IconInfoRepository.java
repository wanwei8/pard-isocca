package com.pard.modules.sys.repository;

import com.pard.modules.sys.entity.IconInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by wawe on 17/5/16.
 */
public interface IconInfoRepository extends PagingAndSortingRepository<IconInfo, String>, JpaSpecificationExecutor<IconInfo> {

}
