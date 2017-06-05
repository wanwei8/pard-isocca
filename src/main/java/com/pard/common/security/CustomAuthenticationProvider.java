package com.pard.common.security;

import com.pard.common.constant.StringConstant;
import com.pard.common.exception.IncorrectCaptchaException;
import com.pard.common.exception.UnknowAccountException;
import com.pard.modules.sys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * Created by wawe on 17/5/3.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider, StringConstant {

    protected Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    @Autowired
    private UserService userService;

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
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), authorities);
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
}
