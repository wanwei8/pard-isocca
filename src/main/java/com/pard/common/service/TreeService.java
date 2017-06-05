package com.pard.common.service;

import com.pard.common.message.Select2;
import com.pard.common.persistence.TreeEntity;

import java.util.List;

/**
 * Created by wawe on 17/5/25.
 */
public interface TreeService<T extends TreeEntity> extends BaseService<T> {

    List<Select2> getTreeSelect(List<T> sources);

    List<Select2> getTreeSelect(List<T> sources, String extId);

    List<Select2> getTreeSelect(List<T> sources, T parent);

    List<Select2> getTreeSelect(List<T> sources, T parent, String extId);
}
