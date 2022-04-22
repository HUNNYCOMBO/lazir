package com.lala.gatherup.domain.account;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@EqualsAndHashCode(of = "id") // 아이디만 사용한다. 성능최적화를 위해
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본값 auto
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String password;

    private String emailCheckToken; // 이메일 인증을 위한 토큰

    private LocalDateTime tokenCreatedTime;

    private String provider;

    @CreationTimestamp
    private Timestamp createDate; // 회원가입 날짜 localdatetime을 써야하는지 고민

    private String profileline; // 자기소개

    @Lob
    @Basic(fetch = FetchType.EAGER) // 즉시 로딩, 메뉴상단에 계속 쓰이기 때문에
    private String profileImage;

    private String location;

    @Enumerated(EnumType.STRING)
    public Role role; // 준회원, 정회원, 관리자 구분

    public Account(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public void createEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.tokenCreatedTime = LocalDateTime.now();
    }

    public boolean isEnableSendEmail() {
        return this.getTokenCreatedTime().isBefore(LocalDateTime.now().minusMinutes(1));
    }

    public boolean isValidToken(String token){
        return this.emailCheckToken.equals(token);
    }

    public void isVerifiedAccount(){
        this.role = Role.REGULAR;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeLocationAndProfileline(String location, String profileline) {
        this.location = location;
        this.profileline = profileline;
    }
}
