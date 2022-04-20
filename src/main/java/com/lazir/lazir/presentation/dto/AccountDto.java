package com.lazir.lazir.presentation.dto;

import com.lazir.lazir.domain.account.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    public static class Response{

    }

    public static class Request{
        private long id;
        private String email;
        private String nickname;
        private String password;
        private String emailCheckToken;
        private LocalDateTime tokenCreatedTime;
        private String provider;
        private Timestamp createDate;
        @Lob
        private String profileline;
        private String profileImage;
        private String location;
        @Enumerated(EnumType.STRING)
        public Role role;
    }
}
