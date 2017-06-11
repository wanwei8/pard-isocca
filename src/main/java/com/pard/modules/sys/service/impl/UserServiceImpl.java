package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.SimpleServiceImpl;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.repository.UserRepository;
import com.pard.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Date;

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

    @Override
    protected String getCacheName() {
        return "users";
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
