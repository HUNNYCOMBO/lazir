package com.lazir.lazir.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class LoginDto {

    @Getter
    @AllArgsConstructor
    public static class Response{
        private long id;
        private String email;
        private String nickname;
    }

    @Data
    public static class Request{
        private long id;
        private String email;
        private String nickname;
        private String password;
    }
}
