package com.pard.common.service.impl;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.pard.common.datatables.DataTablesRepository;
import com.pard.common.exception.ChildNodeExistsException;
import com.pard.common.message.Select2;
import com.pard.common.persistence.TreeEntity;
import com.pard.common.service.TreeService;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.List;

/**
 * Created by wawe on 17/5/25.
 */
public abstract class TreeServiceImpl<T extends TreeEntity, R extends DataTablesRepository> extends SimpleServiceImpl<T, R>
        implements TreeService<T> {
    @Override
    public List<Select2> getTreeSelect(List<T> sources) {
        return getTreeSelect(sources, null, null);
    }

    @Override
    public List<Select2> getTreeSelect(List<T> sources, String extId) {
        return getTreeSelect(sources, null, extId);
    }

    @Override
    public List<Select2> getTreeSelect(List<T> sources, T parent) {
        return getTreeSelect(sources, parent, null);
    }

    @Override
    public List<Select2> getTreeSelect(List<T> sources, T parent, String extId) {
        List<Select2> r = Lists.newArrayList();
        Collection<T> parents;
        if (parent == null) {
            parents = Collections2.filter(sources, x -> null == x.getParent());
        } else {
            parents = Collections2.filter(sources, x -> parent.equals(x.getParent()));
        }
        for (T t : parents) {
            if (extId != null && extId.equals(t.getId())) continue;

            Select2 tree = new Select2();
            tree.setId(t.getId());
            tree.setParent(t.getParentId());
            tree.setText(t.getName());
            r.add(tree);
            //加载子节点
            List<Select2> childSel = getTreeChildSelect(sources, t, extId);
            if (!childSel.isEmpty())
                r.addAll(childSel);
        }
        return r;
    }

    private List<Select2> getTreeChildSelect(List<T> sources, T parent, String extId) {
        Collection<T> childs = Collections2.filter(sources, x -> parent.equals(x.getParent()));
        List<Select2> res = Lists.newArrayList();
        for (T t : childs) {
            if (extId != null && extId.equals(t.getId())) continue;

            Select2 tree = new Select2();
            tree.setParent(parent.getId());
            tree.setText(t.getName());
            tree.setId(t.getId());
            res.add(tree);
            //递归加载子节点
            List<Select2> childSel = getTreeChildSelect(sources, t, extId);
            if (!childSel.isEmpty())
                res.addAll(childSel);
        }
        return res;
    }

    @Override
    public void delete(String id) {
        Specification<T> specification = (root, query, cb) -> {
            List<Predicate> list = Lists.newArrayList();
            list.add(cb.like(root.get("parentIds").as(String.class), "%" + id + ";%"));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        if (getRepository().count(specification) > 0) {
            throw new ChildNodeExistsException();
        }
        getRepository().delete(id);
    }
}
