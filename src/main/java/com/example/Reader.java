package com.example;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by hds on 2016. 11. 15..
 */

@Entity
public class Reader implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id  //Reader 필드
    private String username;
    private String fullname;
    private String password;


    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //READER 권한 부여
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_READER"));
    }

    @Override
    public boolean isAccountNonExpired() { //계정이 만료되지 않았다는 것을 반환
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //계정이 잠겨 있지 않았다는 것을 반환
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //자격이 유효하다는 것을 반환
        return true;
    }

    @Override
    public boolean isEnabled() { //계정이 활성화되어 있다는 것을 반환
        return true;
    }
}
