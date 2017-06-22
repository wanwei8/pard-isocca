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
import com.pard.modules.sys.repository.UserRepository;
import com.pard.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Parameter;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wawe on 17/5/22.
 */
@Component("userService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@CacheConfig(cacheNames = "users")
public class UserServiceImpl extends SimpleServiceImpl<User, UserRepository> implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    @Override
    protected void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @Cacheable
    @Override
    public User findByName(String name) {
        return getRepository().findByName(name);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void saveLoginInfo(String id, String addr, Date date) {
        getRepository().saveLoginInfo(id, addr, date);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void disableUser(String id) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("update User set loginFlag = :loginFlag")
                .append(" where id = :id");
        Query query = entityManager.createQuery(sbHql.toString());

        query.setParameter("loginFlag", User.ACCOUNT_DISABLED);
        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void enableUser(String id) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("update User set loginFlag = :loginFlag")
                .append(" where id = :id");
        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("loginFlag", User.ACCOUNT_NORMAL);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Cacheable
    @Override
    public DataTableResponse<User> findAllUser(DataTableRequest input) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("select u from User u ");
        Map<String, Object> parameters = Maps.newHashMap();

        Column colRole = input.getColumn("role");
        String rid = colRole.getSearch().getValue();
        if (StringUtils.isNotBlank(rid)) {
            sbHql.append("inner join u.roleList r  with (r.id = :roleId) ");
            parameters.put("roleId", rid);
        }
        sbHql.append("Where (u.delFlag = :delFlag) ");
        parameters.put("delFlag", User.DEL_FLAG_NORMAL);

        Column colOffice = input.getColumn("officeId");
        String oid = colOffice.getSearch().getValue();
        if (StringUtils.isNotBlank(oid)) {
            sbHql.append(" and (u.office.parentIds like :officeId or u.office.id = :companyId)");
            parameters.put("officeId", "%" + oid + "%");
            parameters.put("companyId", oid);
        }

        Column colName = input.getColumn("name");
        String name = colName.getSearch().getValue();
        if (StringUtils.isNotBlank(name)) {
            sbHql.append(" and (u.name like :name)");
            parameters.put("name", "%" + name + "%");
        }
        Column colLoginName = input.getColumn("loginName");
        String loginName = colLoginName.getSearch().getValue();
        if (StringUtils.isNotBlank(loginName)) {
            sbHql.append(" and (u.loginName like :loginName)");
            parameters.put("loginName", "%" + loginName + "%");
        }
        Column colNo = input.getColumn("no");
        String no = colNo.getSearch().getValue();
        if (StringUtils.isNotBlank(no)) {
            sbHql.append(" and (u.no like :no)");
            parameters.put("no", "%" + no + "%");
        }
        sbHql.append(" order by u.id asc");

        Query query = entityManager.createQuery(sbHql.toString());
        parameters.forEach((k, v) -> {
            query.setParameter(k, v);
        });
        List<User> lst = query.getResultList();

        DataTableResponse<User> r = new DataTableResponse<>();
        r.setDraw(input.getDraw());
        r.setData(lst);
        r.setRecordsTotal(lst.size());
        r.setRecordsFiltered(lst.size());

        return r;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void deleteUser(String id) {
        StringBuilder sbHql = new StringBuilder();
        sbHql.append("update User u ")
                .append("set u.delFlag = :delFlag ")
                .append("where u.id = :id");

        Query query = entityManager.createQuery(sbHql.toString());
        query.setParameter("delFlag", User.DEL_FLAG_DELETE);
        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Cacheable
    @Override
    public List<User> findByOffice(String id) {
        return getRepository().findByOffice(id, "%" + id + "%");
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void save(User model) {
        if (StringUtils.isNotBlank(model.getNewPassword())) {
            model.setPassword(passwordEncoder.encode(model.getNewPassword()));
        }
        super.save(model);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    @Override
    public void delete(String id) {
        super.delete(id);
    }
}
