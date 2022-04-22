package com.lala.gatherup.infrastructure.config;

// import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.lala.gatherup.presentation.dto.LoginDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;

//시큐리티는 userdetails타입의 오브젝트를 시큐리티 고유세션에 저장한다.
@Getter
public class PrincipalDetail implements UserDetails, OAuth2User{
    // 외부계정(google)과 직접가입한 계정을 둘다 적용할 수있도록 상속받음.
    private final LoginDto.Request account; //컴포지션
    private Map<String, Object> attributes;

    //일반로그인
    public PrincipalDetail(LoginDto.Request account){
        this.account = account;
    }
    //외부로그인
    public PrincipalDetail(LoginDto.Request account, Map<String, Object> attributes){
        this.account = account;
        this.attributes = attributes;
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
        return account.getNickname();
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

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
    
}
