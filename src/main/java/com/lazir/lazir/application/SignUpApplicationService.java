package com.lazir.lazir.application;


import com.lazir.lazir.domain.account.Account;
import com.lazir.lazir.domain.account.AccountService;
import com.lazir.lazir.presentation.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class SignUpApplicationService {

    private final AccountService accountService;
    // 어플리케이션 레이어

    // 회원가입 후 자동 로그인
    @Transactional  // 메소드레벨 > 클래스레벨
    public void signUpAndLogin(SignUpDto.Request singUpRequest) {
        Account account = convertToAccount(singUpRequest);
        accountService.signUp(singUpRequest.getEmail(), singUpRequest.getNickname(), singUpRequest.getPassword());
        accountService.sendEmailForVerify(account);
        accountService.login(accountService.convertToLoginDto(singUpRequest.getEmail()));
    }

    // 이메일 인증 완료
    public void completeSignUp(Account account){
        account.isVerifiedAccount();
        accountService.login(accountService.convertToLoginDto(account.getEmail()));
    }

    private Account convertToAccount(SignUpDto.Request request) {
        return new Account(request.getEmail(), request.getNickname(), request.getPassword());
    }
}
