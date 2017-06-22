package com.pard.modules.sys.repository;

import com.pard.common.datatables.DataTablesRepository;
import com.pard.modules.sys.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wawe on 17/5/3.
 */
public interface UserRepository extends DataTablesRepository<User, String> {

    @Transactional(readOnly = true)
    @Query(value = "select u from User u where u.loginName=:loginName")
    User findByName(@Param("loginName") String loginName);

    @Modifying
    @Query("update User u set u.loginIp=:loginIp, u.loginDate=:loginDate where u.id = :id")
    @Transactional
    int saveLoginInfo(@Param("id") String id, @Param("loginIp") String remoteAddress, @Param("loginDate") Date loginDate);

    @Query("select u from User u where u.office.id = :oid or u.office.parentIds like :pid")
    List<User> findByOffice(@Param("oid") String id, @Param("pid") String pid);
}
