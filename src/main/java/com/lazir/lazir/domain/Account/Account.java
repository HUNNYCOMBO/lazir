package com.lazir.lazir.domain.Account;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data       //setter, getter 만들어 줌
@EqualsAndHashCode(of = "id") // 아이디만 사용한다. 성능최적화를 위해
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
// model 역할을 하는 entity개체
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

    private LocalDateTime createTokenTime;

    private String provider;

    @CreationTimestamp
    private Timestamp createDate; // 회원가입 날짜 localdatetime보다 sql에 어울릴 듯하다.

    private String profileline; // 자기소개

    @Lob
    @Basic(fetch = FetchType.EAGER) // 즉시 로딩/ 메뉴상단에 계속 쓰일 것
    private String profileImage;

    private String location;

    @Enumerated(EnumType.STRING)
    public Role role; // 준회원, 정회원, 관리자 구분

    // private boolean teamCreatedNotice; // TODO태그에 해당하는 팀 생성 알림

    // private boolean teamJoinNotcie; // TODO팀 가입됨 알림

    // @ManyToMany
    // private Set<Tag> tag = new HashSet<>(); 
}
