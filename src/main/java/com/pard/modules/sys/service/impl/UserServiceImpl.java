package com.pard.modules.sys.service.impl;

import com.pard.common.service.impl.SimpleServiceImpl;
import com.pard.common.utils.IdGen;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.repository.UserRepository;
import com.pard.modules.sys.service.UserService;
import com.pard.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by wawe on 17/5/22.
 */
@Component("userService")
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

    @Override
    public User findByName(String name) {
        return getRepository().findByName(name);
    }

    @Override
    public void saveLoginInfo(String id, String addr, Date date) {
        getRepository().saveLoginInfo(id, addr, date);
        clearCache();
    }

    @Override
    public void save(User model) {
        if (StringUtils.isNotBlank(model.getNewPassword())) {
            model.setPassword(passwordEncoder.encode(model.getNewPassword()));
        }
        super.save(model);
    }
}
