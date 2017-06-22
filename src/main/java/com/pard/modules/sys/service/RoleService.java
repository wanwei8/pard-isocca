package com.pard.modules.sys.service;

import com.pard.common.datatables.DataTableRequest;
import com.pard.common.datatables.DataTableResponse;
import com.pard.common.service.BaseService;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wawe on 17/6/12.
 */
public interface RoleService extends BaseService<Role> {
    DataTableResponse<Role> findAllRole(DataTableRequest input);

    long countRoleByName(String name);

    void deleteRole(String id);

    Page<User> findRoleUser(String id, Pageable pageable);

    void outUserInRole(Role role, User user);

    List<User> findRoleUser(String id);

    List<Role> findAll();

    List<Role> findByOffice(String id);

    List<Role> findRoleByUser(String id);

    Page<Role> findRoleByUser(String id, Pageable pageable);

    List<Menu> findMenusByRole(Role role);
}
