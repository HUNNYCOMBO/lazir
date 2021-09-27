package com.lazir.lazir.controller;

import javax.validation.Valid;

import com.lazir.lazir.config.Principal;
import com.lazir.lazir.domain.Account;
import com.lazir.lazir.form.AccountForm;
import com.lazir.lazir.repository.AccountRepository;
import com.lazir.lazir.service.AccountService;
import com.lazir.lazir.validator.AccountValidator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller //HTML 파일을 리턴함
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountValidator AccountValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @InitBinder("accountForm")  //AccountForm요청이 들어올때 binder를 거치게된다.
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(AccountValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("accountForm", new AccountForm());
        //도메인 : 레포지토리에서 불러와 실제 계정처럼 사용하는 객체. 우선 도메인을 쓴다. attrilbute key를 통해 타임리프의 오브잭트로 쓸수 있다.
        //리포지토리 : 실제 db에 접근하여 도메인객체를 저장하거나 불러오는 객체
        //새 도메인 객체를 모델에 담아서 보낸다.
        return "account/sign-up";
    }

    //검증해야할 다수의 객체가 있을땐 modelattribute
    //vaild처리를 해줄 Errors 클래스.

    @PostMapping("/sign-up")
    public String signUpSubmit(@ModelAttribute @Valid AccountForm accountForm, Errors errors) {
        if(errors.hasErrors()){
            return "account/sign-up";
        }

        Account account = accountService.checkEmail(accountForm);
        accountService.login(account);  //회원가입 후 자동 로그인
        return "redirect:/";
    }
    
    @GetMapping("/check-email-token")
    public String checkEmail(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if(account == null){
            model.addAttribute("error", "wrong.email");
            return view;
        }

        if(!account.getEmailCheckToken().equals(token)){
            model.addAttribute("error", "wrong.token");
            return view;
        }

        accountService.setAccountLevel(account);
        accountService.login(account);      //이메일 확인 후 자동 로그인
        model.addAttribute("nickname", account.getNickname());

        return view;
    }

    @GetMapping("/check-email")
    public String checkEmail(@Principal Account account, Model model){
        //pricipal객체의 필드에 email이없어서 값이 넘어가지 않았다.
        model.addAttribute("email", account.getEmail());
        //UserPrincipal객체의 필드 email을 가져옴.
        return "account/check-email";
    }

    @GetMapping("/resend-email")
    public String resendEmail(@Principal Account account, Model model){
        //db에는 cratetokendate가 반영되지않지만 persist상태이기에 작동하는것인듯.
        if(!accountService.sendEmailColl(account)){
           model.addAttribute("error", "인증 메일은 1분에 한번 전송할 수 있습니다.");
           model.addAttribute("email", account.getEmail());
           return "account/check-email";
        }
        
        //중복요청 방지 redirect
        accountService.sendSignUpEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @Principal Account account){
        Account owner = accountRepository.findByNickname(nickname);
        if(owner == null){
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }

        model.addAttribute("account", owner);
        model.addAttribute("owner", owner.equals(account));
        log.info("모델정보"+model.toString());
        return "account/profile";
    }
}
