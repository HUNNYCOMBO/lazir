package com.lazir.lazir.config;

import java.util.ArrayList;
import java.util.Collection;

import com.lazir.lazir.domain.Account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NoArgsConstructor;

//시큐리티는 userdetails타입의 오브젝트를 시큐리티 고유세션에 저장한다.
@NoArgsConstructor
public class PrincipalDetail implements UserDetails{
    private Account account; //컴포지션

    public PrincipalDetail(Account account){
        this.account=account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(()->{return "ROLE_" + account.getRole();});
        //람다식. 매개변수로 메소드를 넘겨줌
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
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
