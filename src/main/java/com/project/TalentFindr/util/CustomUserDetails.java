package com.project.TalentFindr.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.entity.UsersType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {


    private  Users users;


    public CustomUserDetails(Users users) {
        this.users =users;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        UsersType usersType= users.getUserTypeId();
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usersType.getUserTypeName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
