package com.pard.modules.sys.service;

import com.pard.common.service.BaseService;
import com.pard.common.service.TreeService;
import com.pard.modules.sys.entity.Office;

import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
public interface OfficeService extends TreeService<Office> {

    List<Office> findByParentId(String value);

    List<Office> findAllWithTree();

    void updateSort(List<Office> updates);

    List<Office> findCompanyWithTree();

    List<Office> findOfficeWithTree(String cid);
}
