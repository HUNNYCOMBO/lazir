package com.lazir.lazir.service;

import java.util.UUID;

import com.lazir.lazir.config.PrincipalDetail;
import com.lazir.lazir.domain.Account;
import com.lazir.lazir.repository.AccountRepository;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2Service extends DefaultOAuth2UserService{
    
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private Account account;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId();    //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = "g" + providerId;
        String email = oAuth2User.getAttribute("email");
        String password = UUID.randomUUID().toString();
        log.info("google정보 : " + provider + providerId + email + password);

        account = accountRepository.findByEmail(email);

        if(account == null){
            account = createOauthAccount(username, email, password);
            log.info("account 정보 : " + account.getEmail() + account.getNickname() + account.getRole());
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
            .build();

        accountService.setAccountLevel(account);
        //TODO 닉네임변경시에 정회원으로 업그레이드
        accountRepository.save(account);
        return account;
    }
}
