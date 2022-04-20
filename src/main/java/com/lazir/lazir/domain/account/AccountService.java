package com.lazir.lazir.domain.account;

import com.lazir.lazir.presentation.dto.PasswordForm;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Resource
    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender javaMailSender;

    @Transactional
    public void updatePassword(Account account, String password, String passwordCheck) {

        account.levelUpToAdmin();
        repository.save(account);

        account.changePassword()
        this.password = passwordEncoder.encode(passwordForm.getPassword());
        accountRepository.save(account);
    }


    public void signup() {
        checkAccount(account);
        accountRepository.save(account);
        emailClient.sendEmail(account.getEmail(), SIGNUP_VERIFICATION);
    }

    public void checkAccount(Account account) {
        if (account.isInvalidCreatedAt()) {
            throw new Ex;
        }

        // check pw

        if (accountRepository.findByEmail(account.getEmail())) {
            throw new DuplicatedEmailException();
        }
    }
}
