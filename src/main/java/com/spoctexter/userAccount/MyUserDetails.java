package com.spoctexter.userAccount;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class MyUserDetails implements UserDetails {

    private String username;
    private String password;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public MyUserDetails(UserAccount userAccount) {
        this.username = userAccount.getUsername();
        this.password = userAccount.getPassword();
        this.isAccountNonExpired = userAccount.isAccountNonExpired();
        this.isAccountNonLocked = userAccount.isAccountNonLocked();
        this.isCredentialsNonExpired = userAccount.isCredentialsNonExpired();
        this.isEnabled = userAccount.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername(){
        return username;
    };

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
