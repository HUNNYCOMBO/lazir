package com.lala.gatherup.validator;


import com.lala.gatherup.domain.Account;
import com.lala.gatherup.form.ProfileForm;
import com.lala.gatherup.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor    //final 생성자를 만들어줌.(반드시 필요한 것)
public class ProfileValidator implements Validator{
    
    @Autowired
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> class1) {
       return class1.isAssignableFrom(ProfileForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ProfileForm profileForm = (ProfileForm)object;
        Account account = accountRepository.findByEmail(profileForm.getEmail());
        if (accountRepository.existsByNickname(profileForm.getNickname()) && !account.getNickname().equals(profileForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{profileForm.getNickname()}, "이미 사용중인 닉네임입니다.");
        } 
    }


}
