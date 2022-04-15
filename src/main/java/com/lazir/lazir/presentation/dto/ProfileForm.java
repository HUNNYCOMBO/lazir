package com.lazir.lazir.presentation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.lazir.lazir.domain.account.Account;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileForm {
    
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,10}$", message = "공백과 특수문자는 사용할 수 없습니다. 3-10자 사이로 입력해주세요.")
    @Length(min = 3, max = 10)
    private String nickname;

    private String location;

    @Length(min = 0, max = 35)
    private String profileline;

    private String email;

    public ProfileForm(Account account){
        this.location = account.getLocation();
        this.profileline = account.getProfileline();
        this.nickname = account.getNickname();
        this.email = account.getEmail();
    }
}
