package com.pard.modules.sys.service;

import com.pard.common.service.BaseService;
import com.pard.modules.sys.entity.IconInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by wawe on 17/6/6.
 */
public interface IconInfoService {
    Page<IconInfo> findAll(Specification<IconInfo> specification, Pageable pageable);

    Page<IconInfo> findAll(Integer page, Integer size, String category, String icon);

    void save(List<IconInfo> iconInfos);
}
