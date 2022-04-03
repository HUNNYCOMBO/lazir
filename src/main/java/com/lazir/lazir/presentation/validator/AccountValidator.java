package com.lazir.lazir.presentation.validator;


import com.lazir.lazir.domain.Account.AccountRepository;
import com.lazir.lazir.presentation.dto.SingUpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountValidator implements Validator{
    
    @Autowired
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> class1) {
       return class1.isAssignableFrom(SingUpRequest.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SingUpRequest accountform = (SingUpRequest)object;
        if (accountRepository.existsByEmail(accountform.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{accountform.getEmail()}, "이미 사용중인 이메일입니다.");
        }
        if (accountRepository.existsByNickname(accountform.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{accountform.getNickname()}, "이미 사용중인 닉네임입니다.");
        }
    }


}