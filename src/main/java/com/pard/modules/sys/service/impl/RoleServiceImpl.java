package com.pard.modules.sys.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pard.common.datatables.Column;
import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.service.impl.SimpleServiceImpl;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.repository.RoleRepository;
import com.pard.modules.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

/**
 * Created by wawe on 17/6/12.
 */
@Component("roleService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@CacheConfig(cacheNames = "roles")
public class RoleServiceImpl extends SimpleServiceImpl<Role, RoleRepository> implements RoleService {

    @Autowired
    @Override
    protected void setRepository(RoleRepository repository) {
        this.repository = repository;
    }

    @Cacheable
    @Override
    public DataTableResponse<Role> findAllRole(DataTableRequest input) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select r from Role r ")
                .append("Where (r.delFlag = :delFlag) ");
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("delFlag", Role.DEL_FLAG_NORMAL);

        DataTableResponse<Role> r = new DataTableResponse<>();
        r.setDraw(input.getDraw());

        Column colOffice = input.getColumn("officeId");
        String oid = colOffice.getSearch().getValue();
        if (StringUtils.isNotBlank(oid)) {
            sbHql.append(" and (r.office.parentIds like :officeId or r.office.id = :companyId)");
            parameters.put("officeId", "%" + oid + "%");
            parameters.put("companyId", oid);
        }
        Column colName = input.getColumn("name");
        String name = colName.getSearch().getValue();
        if (StringUtils.isNotBlank(name)) {
            sbHql.append(" and (r.name like :name)");
            parameters.put("name", "%" + name + "%");
        }
        sbHql.append(" order by id asc");

        Query query = entityManager.createQuery(sbHql.toString());
        parameters.forEach((k, v) -> {
            query.setParameter(k, v);
        });
        List<Role> lst = query.getResultList();

        r.setData(lst);
        r.setRecordsTotal(lst.size());
        r.setRecordsFiltered(lst.size());

        return r;
    }

    @Cacheable
    @Override
    public long countRoleByName(String name) {
        Specification<Role> specification = new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("name").as(String.class), name);
                return criteriaBuilder.and(predicate);
            }
        };
        return getRepository().count(specification);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void deleteRole(String id) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("update Role u ")
                .append("set u.delFlag = :delFlag ")
                .append("where u.id = :id");

        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("delFlag", User.DEL_FLAG_DELETE);
        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Cacheable
    @Override
    public Page<User> findRoleUser(String id, Pageable pageable) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select r.userList from Role r where r.id = :id");
        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("id", id);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        Page<User> pageRst = new PageImpl<User>(query.getResultList(), pageable, query.getResultList().size());

        return pageRst;
    }

    @Override
    public void outUserInRole(Role role, User user) {
        if (role.getUserList().indexOf(user) >= 0) {
            role.getUserList().remove(user);
            save(role);
        }
    }

    @Cacheable
    @Override
    public List<User> findRoleUser(String id) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select r.userList from Role r where r.id = :id");
        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Cacheable
    @Override
    public List<Role> findAll() {
        StringBuilder sbHql = new StringBuilder();

        sbHql.append("select r from Role r where r.delFlag = :delFlag order by r.name");

        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("delFlag", Role.DEL_FLAG_NORMAL);

        return query.getResultList();
    }

    @Cacheable
    @Override
    public List<Role> findByOffice(String id) {
        return getRepository().findByOffice(id, "%" + id + "%");
    }

    @Override
    public List<Role> findRoleByUser(String id) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select u.roleList from User u where u.id = :id");
        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("id", id);
        return Lists.newArrayList(query.getResultList());
    }

    @Cacheable
    @Override
    public Page<Role> findRoleByUser(String id, Pageable pageable) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select u.roleList from User u where u.id = :id");
        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("id", id);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        Page<Role> pageRst = new PageImpl<Role>(query.getResultList(), pageable, query.getResultList().size());

        return pageRst;
    }

    @Cacheable
    @Override
    public List<Menu> findMenusByRole(Role role) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select r.menuList from Role r where r.id = :id");
        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("id", role.getId());
        return query.getResultList();
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void save(Role model) {
        super.save(model);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void delete(String id) {
        super.delete(id);
    }
}
