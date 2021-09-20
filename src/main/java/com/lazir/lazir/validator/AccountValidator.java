package com.lazir.lazir.validator;


import com.lazir.lazir.form.AccountForm;
import com.lazir.lazir.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor    //final 생성자를 만들어줌.(반드시 필요한 것)
public class AccountValidator implements Validator{
    
    @Autowired
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> class1) {
       return class1.isAssignableFrom(AccountForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        AccountForm accountform = (AccountForm)object;
        if (accountRepository.existsByEmail(accountform.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{accountform.getEmail()}, "이미 사용중인 이메일입니다.");
        }
        //TODO errors 공부
        if (accountRepository.existsByNickname(accountform.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{accountform.getEmail()}, "이미 사용중인 닉네임입니다.");
        } 
    }


}
