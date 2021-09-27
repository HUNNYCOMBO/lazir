package com.lazir.lazir.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class AccountForm {
    
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,10}$")
    @Length(min = 3, max = 10)
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{6,20}$")
    @Length(min = 6, max = 20)
    private String password;

    private String emailCheckToken;
}
