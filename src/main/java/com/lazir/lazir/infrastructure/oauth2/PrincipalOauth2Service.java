package com.lazir.lazir.infrastructure.oauth2;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lazir.lazir.domain.account.AccountService;
import com.lazir.lazir.domain.account.Role;
import com.lazir.lazir.infrastructure.config.PrincipalDetail;
import com.lazir.lazir.domain.account.Account;
import com.lazir.lazir.domain.account.AccountRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2Service extends DefaultOAuth2UserService{
    
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private Account account;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String username = oAuth2User.getAttribute("sub");
        String email = "g_" + oAuth2User.getAttribute("email");
        String password = UUID.randomUUID().toString();

        account = accountRepository.findByEmail(email);

        if(account == null){
            account = createOauthAccount(username, email, password);
        }

        return new PrincipalDetail(accountService.convertToLoginDto(email), oAuth2User.getAttributes());
    }

    @Transactional
    private Account createOauthAccount(String username, String email, String password) {
        account = Account.builder()
            .email(email)
            .nickname(username)
            .password(password)
            .provider(username)
            .tokenCreatedTime(LocalDateTime.now())
            .role(Role.REGULAR)
            .build();

        accountRepository.save(account);
        return account;
    }
}
