package com.lazir.lazir.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lazir.lazir.config.PrincipalDetail;
import com.lazir.lazir.domain.Account.Account;
import com.lazir.lazir.domain.Account.AccountRepository;
import com.lazir.lazir.domain.Account.AccountService;

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

        return new PrincipalDetail(account, oAuth2User.getAttributes());
    }

    @Transactional
    private Account createOauthAccount(String username, String email, String password) {
        account = Account.builder()
            .email(email)
            .nickname(username)
            .password(password)
            .provider(username)
            .createTokenTime(LocalDateTime.now())
            .build();

        accountService.setAccountLevel(account);
        //TODO 닉네임변경시에 정회원으로 업그레이드
        accountRepository.save(account);
        return account;
    }
}
