package com.lazir.lazir.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class AccountForm {
    
    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,10}$", message = "공백과 특수문자는 사용할 수 없습니다. 3-10자 사이로 입력해주세요.")
    @Length(min = 3, max = 10)
    private String nickname;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{6,20}$", message = "영어와 숫자만 입력해주세요. 6-20자 사이로 입력해주세요.")
    @Length(min = 6, max = 20)
    private String password;

    private String emailCheckToken;
}
