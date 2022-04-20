package com.lazir.lazir.domain.account;

import com.lazir.lazir.infrastructure.config.PrincipalDetail;
import com.lazir.lazir.infrastructure.mail.ConsoleMailSender;
import com.lazir.lazir.infrastructure.mail.ConsoleMailService;
import com.lazir.lazir.presentation.dto.AccountDto;
import com.lazir.lazir.presentation.dto.LoginDto;
import com.lazir.lazir.presentation.dto.PasswordForm;
import com.lazir.lazir.presentation.dto.ProfileForm;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConsoleMailService consoleMailService;

    // 인증 메일 발송 로직
    public void sendEmailForVerify(Account account) {
        account.createEmailCheckToken();
        accountRepository.save(account);
        consoleMailService.verifyEmailSet(account.getEmail(), account.getEmailCheckToken());
    }

    // 로그인로직
    public void login(LoginDto.Request account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new PrincipalDetail(account), account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole())));
        // principal로 user를 상속받은 객체를 사용, password, 권한
         SecurityContextHolder.getContext().setAuthentication(token);
        // AuthenticationManager와 하는 일이 같아짐.
    }

    // 프로필 수정 로직
    public void updateProfile(Account account, @Valid ProfileForm profileForm) {
        // 여기의 account는 principal에서 온 account이기에 persist상태가 아닌, defatched상태
        if (!account.getNickname().equals(profileForm.getNickname())
                && !accountRepository.existsByNickname(profileForm.getNickname())) {
            account.changeNickname(profileForm.getNickname());
        }
        account.changeLocationAndProfileline(profileForm.getLocation(), profileForm.getProfileline());
        accountRepository.save(account);
    }

    // 비밀번호 수정
    public void updatePassword(Account account, @Valid PasswordForm passwordForm) {
        String newPassword = passwordEncoder.encode(passwordForm.getPassword());
        account.changePassword(newPassword);
        accountRepository.save(account);
    }

    // 이메일로 로그인 로직
    public void sendLoginByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        account.createEmailCheckToken();
        consoleMailService.loginByEmailSet(account.getEmail(), account.getEmailCheckToken());
    }

    // 계정 삭제
    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

    // 회원 생성 로직
    public void signUp(String email, String nickname, String password) {
        Account account = Account.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .role(Role.ASSOCIATE)
                .build();

        accountRepository.save(account);
    }

    public LoginDto.Request convertToLoginDto(String email){
        Account account = accountRepository.findByEmail(email);
        return LoginDto.Request.builder()
                .id(account.getId())
                .email(account.getEmail())
                .nickname(account.getNickname())
                .password(account.getPassword())
                .role(account.getRole())
                .build();
    }
}
