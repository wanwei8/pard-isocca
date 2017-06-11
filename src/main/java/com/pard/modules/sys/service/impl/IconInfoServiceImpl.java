package com.pard.modules.sys.service.impl;

import com.google.common.collect.Lists;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.IconInfo;
import com.pard.modules.sys.repository.IconInfoRepository;
import com.pard.modules.sys.service.IconInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by wawe on 17/6/6.
 */
@Component("iconInfoService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@CacheConfig(cacheNames = "iconInfoes")
public class IconInfoServiceImpl implements IconInfoService {

    @Autowired
    private IconInfoRepository repository;

    @Cacheable
    @Override
    public Page<IconInfo> findAll(Specification<IconInfo> specification, Pageable pageable) {
        return repository.findAll(specification, pageable);
    }

    @Cacheable
    @Override
    public Page<IconInfo> findAll(Integer page, Integer size, String category, String icon) {
        Pageable pageable = new PageRequest(page, size);
        Specification<IconInfo> specification = new Specification<IconInfo>() {
            @Override
            public Predicate toPredicate(Root<IconInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayList();
                if (StringUtils.isNotBlank(category) && !"search".equals(category)) {
                    predicates.add(cb.equal(root.get("sourceType").as(String.class), category));
                }
                if (StringUtils.isNotBlank(icon)) {
                    predicates.add(cb.like(root.get("displayName").as(String.class), "%" + icon + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return repository.findAll(specification, pageable);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void save(List<IconInfo> iconInfos) {
        repository.save(iconInfos);
    }
}
