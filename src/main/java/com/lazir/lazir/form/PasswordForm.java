package com.lazir.lazir.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class PasswordForm {
    
    @NotNull
    @NotBlank
    @Length(min = 6, max = 20)
    @Pattern(regexp = "^[a-z0-9]{6,20}$", message = "영어와 숫자만 입력해주세요. 6-20자 사이로 입력해주세요.")
    private String password;

    @NotNull
    @NotBlank
    @Length(min = 6, max = 20)
    @Pattern(regexp = "^[a-z0-9]{6,20}$", message = "영어와 숫자만 입력해주세요. 6-20자 사이로 입력해주세요.")
    private String passwordCheck;

}
