package com.lazir.lazir.service;

import com.lazir.lazir.config.PrincipalDetail;
import com.lazir.lazir.domain.Account;
import com.lazir.lazir.repository.AccountRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService{

    private final AccountRepository accountRepository;

    //시큐리티가 로그인요청을 가로챌때, username이 db에있는지 확인.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account principal = accountRepository.findByEmail(email);
        return new PrincipalDetail(principal);  //시큐리티 세션에 정보 저장 됨.
    }
    
}
