package com.pard.common.security;

import com.google.common.collect.Sets;
import com.pard.modules.sys.entity.Menu;
import com.pard.modules.sys.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by wawe on 17/5/3.
 */
public class CustomUserDetails extends User implements UserDetails {

    private final Set<GrantedAuthority> grantedAuthorities;

    private final Set<Menu> menus;

    public CustomUserDetails(User user) {
        super(user);
        grantedAuthorities = Sets.newHashSet();
        menus = Sets.newHashSet();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getLoginFlag().equals(YES);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return getDelFlag().equals(DEL_FLAG_NORMAL);
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public Set<Menu> getMenus() {
        return menus;
    }
}
