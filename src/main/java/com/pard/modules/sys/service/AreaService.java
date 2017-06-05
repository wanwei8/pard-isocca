package com.pard.modules.sys.service;

import com.pard.common.service.TreeService;
import com.pard.modules.sys.entity.Area;

import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
public interface AreaService extends TreeService<Area> {

    List<Area> findByParentId(String pid);

    List<Area> findAllWithTree();

    void updateSort(List<Area> updates);
}
