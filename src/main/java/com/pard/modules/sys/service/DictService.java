package com.pard.modules.sys.service;

import com.pard.common.service.BaseService;
import com.pard.modules.sys.entity.Dict;

import java.util.List;

/**
 * Created by wawe on 17/5/22.
 */
public interface DictService extends BaseService<Dict> {

    int findMaxSortByType(String type);

    void save(Dict dict);

    List<String> findDistinctDictType();

    List<Dict> findByType(String type);
}
