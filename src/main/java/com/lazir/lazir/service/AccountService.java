package com.lazir.lazir.service;

import java.util.UUID;

import javax.validation.Valid;

import com.lazir.lazir.domain.Account;
import com.lazir.lazir.domain.Role;
import com.lazir.lazir.form.AccountForm;
import com.lazir.lazir.repository.AccountRepository;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public void generateEmailCheckToken(Account account) {
          account.setEmailCheckToken(UUID.randomUUID().toString());
    }

    private Account createNewAccount(@Valid AccountForm accountForm){
        Account account = Account.builder()   
        .email(accountForm.getEmail())
        .nickname(accountForm.getNickname())
        .password(passwordEncoder.encode(accountForm.getPassword()))
        .emailCheckToken(accountForm.getEmailCheckToken())
        .teamCreatedNotice(false)
        .teamJoinNotcie(false)
        .build();
        
        generateEmailCheckToken(account);
        account.setRole(Role.ASSOCIATE);

        return accountRepository.save(account); //여기까지 persist상태
    }

    private void sendSignUpEmail(Account newAccount){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(newAccount.getEmail());
        simpleMailMessage.setSubject("lazir 회원가입 인증");
        simpleMailMessage.setText("/check-email?token=" + newAccount.getEmailCheckToken() + 
        "&email=" + newAccount.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    @Transactional  //persist 상태 유지.
    public Account checkEmail(AccountForm accountForm){
        Account newAccount = createNewAccount(accountForm);
        sendSignUpEmail(newAccount);
        return newAccount;
    }


    public void setAccountLevel(Account account){
        account.setRole(Role.REGULAR);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken
            (account.getEmail(), account.getPassword());  //principal, password, 권한 
        
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
