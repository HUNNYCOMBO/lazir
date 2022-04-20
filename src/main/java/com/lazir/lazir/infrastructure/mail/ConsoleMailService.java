package com.lazir.lazir.infrastructure.mail;

import com.lazir.lazir.domain.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsoleMailService {
    private final ConsoleMailSender consoleMailSender;
    private final String checkEmailURL = "/check-email-token";
    private final String checkEmailSubject = "lazir 회원가입 인증";
    private final String loginByEmailURL = "/login-by-email-token";
    private final String loginByEmailSubject = "lazir 이메일로 로그인하기";

    public void verifyEmailSet(String email, String token) {
        SimpleMailMessage simpleMailMessage = mailSetWithToken(email, checkEmailSubject, checkEmailURL, token);
        consoleMailSender.send(simpleMailMessage);
    }

    public SimpleMailMessage mailSetWithToken(String email, String subject, String URL, String token) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage
                .setText(URL + "?token=" + token + "&email=" + email);
        return simpleMailMessage;
    }

    public void loginByEmailSet(String email, String token){
        SimpleMailMessage simpleMailMessage = mailSetWithToken(email, loginByEmailSubject, loginByEmailURL, token);
        consoleMailSender.send(simpleMailMessage);
    }
}
