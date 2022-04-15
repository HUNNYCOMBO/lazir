package com.lazir.lazir.application;


import com.lazir.lazir.domain.account.Account;
import com.lazir.lazir.presentation.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class AccountService {

    // 어플리케이션 레이어

    // 회원가입 usecase
    @Transactional  // 메소드레벨 > 클래스레벨
    public void signUpAndLogin(SignUpDto.Request singUpRequest){
        Account account =
        account.createTempAccount(singUpRequest);
        account.sendEmailForVerify(account);
        account.login(account);
    }

    public void completeSignUp(Account account){
        account.setVerifiedAccount();
        accountService.login(account);
    }
}
