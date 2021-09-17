package com.lazir.lazir.domain;

import java.sql.Timestamp;
import java.util.Set;

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
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data       //setter, getter 만들어 줌
@EqualsAndHashCode(of = "id") // 아이디만 사용한다.
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
    @Email
    @NotBlank
    private String email;

    @Column(unique = true, nullable = false, length = 30)
    @NotBlank
    @Length(min = 3, max = 10)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-](3,10)$")
    private String nickname;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Length(min = 6, max = 20)
    @Pattern(regexp = "^[a-z0-9](6,20)$")
    private String password; // TODO 해싱처리 해야함

    private String emailCheckToken; // 이메일 인증을 위한 토큰

    private boolean emailVerified; // 이메일 인증된 계정 체크

    @CreationTimestamp
    private Timestamp createDate; // 회원가입 날짜 localdatetime보다 sql에 어울릴 듯하다.

    private String profileline; // 자기소개

    private String location;

    @Lob
    @Basic(fetch = FetchType.EAGER) // 즉시 로딩/ 메뉴상단에 계속 쓰일 것
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role; // admin, user 구분

    private boolean teamCreatedNotice; // TODO태그에 해당하는 팀 생성 알림

    private boolean teamJoinNotcie; // TODO팀 가입됨 알림

    @ManyToMany
    private Set<Tag> tag; // https://joont92.github.io/jpa/%EC%BB%AC%EB%A0%89%EC%85%98%EA%B3%BC-%EB%B6%80%EA%B0%80%EA%B8%B0%EB%8A%A5/
                          // 위와같은 문제가있지만 조인테이블에 데이터를 추가할 일은 없으므로 사용해도 될듯하다.
}
