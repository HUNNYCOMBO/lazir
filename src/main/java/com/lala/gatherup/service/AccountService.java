package com.lala.gatherup.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.lala.gatherup.config.PrincipalDetail;
import com.lala.gatherup.domain.Account;
import com.lala.gatherup.domain.Role;
import com.lala.gatherup.form.AccountForm;
import com.lala.gatherup.form.PasswordForm;
import com.lala.gatherup.form.ProfileForm;
import com.lala.gatherup.repository.AccountRepository;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public void generateEmailCheckToken(Account account) {
        account.setEmailCheckToken(UUID.randomUUID().toString());
    }

    private Account createNewAccount(@Valid AccountForm accountForm) {
        Account account = Account.builder().email(accountForm.getEmail()).nickname(accountForm.getNickname())
                .password(passwordEncoder.encode(accountForm.getPassword()))
                .emailCheckToken(accountForm.getEmailCheckToken())
                // .teamCreatedNotice(false)
                // .teamJoinNotcie(false)
                .build();

        account.setRole(Role.ASSOCIATE);

        return accountRepository.save(account); // 여기까지 persist상태
    }

    public void sendSignUpEmail(Account account) {
        generateEmailCheckToken(account);
        account.setCreateTokenTime(LocalDateTime.now());
        accountRepository.save(account);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(account.getEmail());
        simpleMailMessage.setSubject("lazir 회원가입 인증");
        simpleMailMessage
                .setText("/check-email-token?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    @Transactional // persist 상태 유지.
    public Account checkEmail(AccountForm accountForm) {
        Account newAccount = createNewAccount(accountForm);
        sendSignUpEmail(newAccount);
        return newAccount;
    }

    @Transactional
    public void setAccountLevel(Account account) {
        account.setRole(Role.REGULAR);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new PrincipalDetail(account), account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole()))); // principal로 user를 상속받은 객체를 사용,
                                                                                   // password, 권한
        SecurityContextHolder.getContext().setAuthentication(token);
        // AuthenticationManager와 하는 일이 같아짐.
    }

    public boolean sendEmailColl(Account account) {
        return account.getCreateTokenTime().isBefore(LocalDateTime.now().minusMinutes(1));
    }

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

    public void updatePassword(Account account, @Valid PasswordForm passwordForm) {
        account.setPassword(passwordEncoder.encode(passwordForm.getPassword()));
        accountRepository.save(account);
    }

    public void sendLogInLink(String email) {
        Account account = accountRepository.findByEmail(email);
        generateEmailCheckToken(account);
        account.setCreateTokenTime(LocalDateTime.now());
        accountRepository.save(account);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(account.getEmail());
        simpleMailMessage.setSubject("lazir 이메일으로 로그인하기");
        simpleMailMessage
                .setText("/login-by-email?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail());
        javaMailSender.send(simpleMailMessage);
    }

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }

}
