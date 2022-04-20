package com.lazir.lazir.domain.account;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.Valid;

import com.lazir.lazir.infrastructure.config.PrincipalDetail;
import com.lazir.lazir.presentation.dto.PasswordForm;
import com.lazir.lazir.presentation.dto.ProfileForm;
import com.lazir.lazir.presentation.dto.SignUpDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Getter
@EqualsAndHashCode(of = "id") // 아이디만 사용한다. 성능최적화를 위해
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@RequiredArgsConstructor
// TODO (완) setter를 제거
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

    // TODO (완)enum을 제거
    private String role;  //

    public Account(String email, String nickname, String password, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImage = profileImage;
    }

    public void createEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public boolean isEnableSendEmail() {
        return this.getTokenCreatedTime().isBefore(LocalDateTime.now().minusMinutes(1));
    }

    public boolean isValidToken(String token){
        return this.emailCheckToken.equals(token);
    }

    // ADMIN, 상위, 하위
    public void levelUpToAdmin() {
        // domain model paatern
        if (createDate < 1Y) {
            throw new IllegalStateException("어드민은 경력이 1년 이상인 회원만 가능합니다.");
        }
        if (role == "하위") {
            //
        }

        this.role = "ADMIN";
    }
    // 포트폴리오 만들기 - 한줄 자기소개, github link, blog link, (대표프로젝트 설명) lazir project

    // Java Exception, CheckedException, UnCheckedException
    // TODO JVM 메모리구조. GC(깊게 공부할 필요는 없고, 간단히)
    // Java stream api vs collection framework
    // interface default method 나온 이유
    // Java Generic Erasure(깊게는 필요 없고, 이 단어가 왜 생겻는지, 개발 시 유의할점과 해결 방법)
    // Java 9, 10, 11 새로나온 기능
    // TODO spring의 트랜잭션 작동 원리, 트랜잭션 어노테이션 사용시 주의점
    // TODO trasaction script pattern vs domain model pattern
    // REST API & HTTP 중요한 staus, 의미
    // TCP 3way-handshake, 4way-handshake



    // jpa 영속성 컨텍스트(1차캐시), dirty checking, osiv, n + 1

    // 인증 메일 발송 로직, 메소드이름 변경.
    public void sendEmailForVerify(Account account) {
        setEmailCheckTokenAndTime(account);

        extracted(account);
    }

    // 이메일 인증 토큰 설정 로직
    private void setEmailCheckTokenAndTime(Account account) {
        account.createEmailCheckToken();    // 이메일 재전송하는 경우를 위해 builder에서 분리함.
        account.createTokenCreatedTime();
        // accountRepository.save(account);    // TODO save를 두번 호출해도 괜찮은지 알아볼 것.
    }

    private void createTokenCreatedTime() {
        this.tokenCreatedTime = LocalDateTime.now();
    }


    // 로그인로직
    public void login(Account account) {
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                new PrincipalDetail(account), account.getPassword(),
//                List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole())));
        // principal로 user를 상속받은 객체를 사용, password, 권한
        // SecurityContextHolder.getContext().setAuthentication(token);
        // AuthenticationManager와 하는 일이 같아짐.
    }

    // 프로필 수정 로직
    public void updateProfile(Account account, @Valid ProfileForm profileForm) {
        // 여기의 account는 principal에서 온 account이기에 persist상태가 아닌, defatched상태
        if (!account.getNickname().equals(profileForm.getNickname())
                && !accountRepository.existsByNickname(profileForm.getNickname())) {
            account.setNickname(profileForm.getNickname());
        }
        account.setLocation(profileForm.getLocation());
        account.setProfileline(profileForm.getProfileline());
        accountRepository.save(account);
    }

    // 비밀번호 수정
    public void updatePassword(Account account, @Valid PasswordForm passwordForm) {
        this.password = passwordEncoder.encode(passwordForm.getPassword());
        accountRepository.save(account);
    }

    // 이메일로 로그인 로직
    public void sendLoginByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        setEmailCheckTokenAndTime(account);

        // TODO infra로 이동
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(account.getEmail());
        simpleMailMessage.setSubject("lazir 이메일으로 로그인하기");
        simpleMailMessage
                .setText("/login-by-email?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    // 계정 삭제
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    private void verifyEmailSet(Account account) {
        // TODO infra로 분리
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(getEmail());
        simpleMailMessage.setSubject("lazir 회원가입 인증");
        simpleMailMessage
                .setText("/check-email-token?token=" + getEmailCheckToken() + "&email=" + getEmail());
        account.javaMailSender.send(simpleMailMessage);
    }
}
