package com.lazir.lazir.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.lazir.lazir.domain.Account;
import com.lazir.lazir.repository.AccountRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller //HTML 파일을 리턴함
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountRepository accountRepository;

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("Account", new Account());
        //TODO 도메인을 넣어야할까 모델을 넣어야할까.. 사용용도가 다르긴 하지만 같은 역할을 한다.
        //도메인 : 레포지토리에서 불러와 실제 계정처럼 사용하는 객체. 우선 도메인을 쓴다. attrilbute key를 통해 타임리프의 오브잭트로 쓸수 있다.
        //리포지토리 : 실제 db에 접근하여 도메인객체를 저장하거나 불러오는 객체
        //새 도메인 객체를 모델에 담아서 보낸다.
        log.info("get요청");
        return "account/sign-up";
    }

    //검증해야할 다수의 객체가 있을땐 modelattribute
    //vaild처리를 해줄 Errors 클래스.
    @Transactional
    @PostMapping(value="/sign-up")
    public String signUpSubmit(@ModelAttribute @Valid Account signup, Errors errors) {
        if(errors.hasErrors()){
            log.info("valid처리");
            return "account/sign-up";
        }
        
        log.info("계정저장시작");
        Account account = Account.builder()
        .email(signup.getEmail())
        .nickname(signup.getNickname())
        .password(signup.getPassword())
        .build();

        log.info("계정저장");
        Account newAccount = accountRepository.save(account);
        return "redirect:/";
    }
    
}
