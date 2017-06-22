package com.pard.common.security;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.pard.common.constant.StringConstant;
import com.pard.common.exception.IncorrectCaptchaException;
import com.pard.common.exception.UnknowAccountException;
import com.pard.common.utils.StringUtils;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.Role;
import com.pard.modules.sys.service.MenuService;
import com.pard.modules.sys.service.RoleService;
import com.pard.modules.sys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by wawe on 17/5/3.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider, StringConstant {

    protected Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    private CustomUserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        String token = details.getToken();
        String captcha = (String) details.getSession().getAttribute(SESSION_VALIDATE_CODE);
        if (captcha != null && !captcha.equalsIgnoreCase(token)) {
            throw new IncorrectCaptchaException("验证码错误");
        }
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new UnknowAccountException("用户不存在");
        }
        if (ACCOUNT_DISABLED.equals(user.getLoginFlag())) {
            throw new DisabledException("账户不可用");
        }
        if (ACCOUNT_LOCKED.equals(user.getLoginFlag())) {
            throw new LockedException("账户被锁定");
        }
        if (passwordEncoder.matches(user.getPassword(), password)) {
            throw new BadCredentialsException("用户名/密码错误");
        }
        userService.saveLoginInfo(user.getId(), details.getRemoteAddress(), new Date());
        //加载用户权限
        getUserAuthorities(user);

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    public CustomUserDetailsService getUserService() {
        return userDetailsService;
    }

    public void setUserService(CustomUserDetailsService userService) {
        this.userDetailsService = userService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private void getUserAuthorities(CustomUserDetails user) {
        final Set<Menu> menus = Sets.newHashSet(menuService.findAllMenu());
        if (user.isAdmin()) {
            user.getMenus().addAll(menus);
        } else {
            List<Role> roles = roleService.findRoleByUser(user.getId());

            roles.forEach(role -> {
                List<Menu> roleMenus = roleService.findMenusByRole(role);
                roleMenus.forEach(menu -> {
                    user.getMenus().addAll(getMenuTree(menus, menu, user.getMenus()));
                });
            });
        }

        user.getMenus().forEach(menu -> {
            if (StringUtils.isNotBlank(menu.getPermission())) {
                user.getGrantedAuthorities().add(new SimpleGrantedAuthority(menu.getPermission()));
            }
        });
    }

    private Set<Menu> getMenuTree(Set<Menu> source, Menu current, Set<Menu> extMenus) {
        Set<Menu> rest = Sets.newHashSet();
        if (unexistMenu(extMenus, current)) {
            rest.add(current);
        }
        if (StringUtils.isNotBlank(current.getParentIds())) {
            String[] parentIds = current.getParentIds().split("[;]");
            for (int i = 0; i < parentIds.length; i++) {
                String id = parentIds[i];
                Sets.filter(source, new Predicate<Menu>() {
                    @Override
                    public boolean apply(Menu menu) {
                        return menu.getId().equals(id);
                    }
                }).forEach(menu -> {
                    if (unexistMenu(extMenus, menu)) {
                        rest.add(menu);
                    }
                });
            }
        }
        return rest;
    }

    private boolean unexistMenu(Set<Menu> menus, Menu mn) {
        return Sets.filter(menus, new Predicate<Menu>() {
            @Override
            public boolean apply(Menu menu) {
                return menu.getId().equals(mn.getId());
            }
        }).isEmpty();
    }
}
