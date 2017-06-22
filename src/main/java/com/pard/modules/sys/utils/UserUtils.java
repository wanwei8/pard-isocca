package com.pard.modules.sys.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.pard.common.security.CustomUserDetails;
import com.pard.common.utils.SpringContextHolder;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.User;
import com.pard.modules.sys.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by wawe on 17/4/22.
 */
@Component
public class UserUtils {

    public static CustomUserDetails getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        return null;
    }
}
