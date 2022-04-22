package com.lala.gatherup.presentation.validator;


import com.lala.gatherup.domain.account.Account;
import com.lala.gatherup.domain.account.AccountRepository;
import com.lala.gatherup.presentation.dto.EmailLogInForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailLogInValidator implements Validator{
    
    private final AccountRepository accountRepository;
    private final Account.AccountService accountService;

    @Override
    public boolean supports(Class<?> class1) {
       return class1.isAssignableFrom(EmailLogInForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        EmailLogInForm emailLogIn = (EmailLogInForm)object;
        Account account = accountRepository.findByEmail(emailLogIn.getEmail());
        if(account == null){
            errors.rejectValue("email", "invaild.email", new Object[]{emailLogIn.getEmail()}, "해당 계정이 존재하지 않습니다.");
        }
        if(account.getProvider() != null){
            errors.rejectValue("email", "invaild.email", new Object[]{emailLogIn.getEmail()}, "소셜로그인 계정입니다.");
        }
        if(!account.sendEmailColl()){
            errors.rejectValue("email", "invaild.email", new Object[]{emailLogIn.getEmail()}, "이메일은 1분에 한 번 보낼수 있습니다.");
        }
    }


}
