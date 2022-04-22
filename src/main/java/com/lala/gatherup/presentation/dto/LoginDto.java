package com.lala.gatherup.presentation.dto;

import com.lala.gatherup.domain.account.Role;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    public static class Response{
        private long id;
        private String email;
        private String nickname;
    }

    @Builder
    @Getter
    public static class Request{
        private long id;
        private String email;
        private String nickname;
        private String password;
        private Role role;
    }
}
