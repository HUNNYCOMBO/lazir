package com.lazir.lazir.application;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class AccountApplicationService {

    @Transactional  // 메소드레벨 > 클래스레벨
    public void singUp(){
        Account account = accountService.checkEmail(accountForm);
        accountService.login(account);  //회원가입 후 자동 로그인
    }
}
