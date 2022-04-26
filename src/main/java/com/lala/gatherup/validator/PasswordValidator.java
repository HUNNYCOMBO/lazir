package com.lala.gatherup.validator;


import com.lala.gatherup.form.PasswordForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor    //final 생성자를 만들어줌.(반드시 필요한 것)
public class PasswordValidator implements Validator{
    
    @Override
    public boolean supports(Class<?> class1) {
       return class1.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        PasswordForm passwordForm = (PasswordForm)object;
        if (!passwordForm.getPassword().equals(passwordForm.getPasswordCheck())){
            errors.rejectValue("password", "wrong.password", new Object[]{passwordForm.getPassword()}, "비밀번호가 일치하지 않습니다.");
        } 
    }


}
