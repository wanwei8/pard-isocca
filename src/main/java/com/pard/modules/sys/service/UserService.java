package com.pard.modules.sys.service;

import com.pard.common.service.BaseService;
import com.pard.modules.sys.entity.User;

import java.util.Date;

/**
 * Created by wawe on 17/5/22.
 */
public interface UserService extends BaseService<User> {
    User findByName(String name);

    void saveLoginInfo(String id, String addr, Date date);

    void disableUser(String id);

    void enableUser(String id);
}
