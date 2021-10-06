package com.lazir.lazir.controller;

import java.util.List;

import javax.validation.Valid;

import com.lazir.lazir.config.Principal;
import com.lazir.lazir.domain.Account;
import com.lazir.lazir.domain.Team;
import com.lazir.lazir.form.AccountForm;
import com.lazir.lazir.form.EmailLogInForm;
import com.lazir.lazir.form.PasswordForm;
import com.lazir.lazir.form.ProfileForm;
import com.lazir.lazir.repository.AccountRepository;
import com.lazir.lazir.repository.TeamRepository;
import com.lazir.lazir.service.AccountService;
import com.lazir.lazir.validator.AccountValidator;
import com.lazir.lazir.validator.EmailLogInValidator;
import com.lazir.lazir.validator.PasswordValidator;
import com.lazir.lazir.validator.ProfileValidator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller //HTML 파일을 리턴함
@RequiredArgsConstructor
public class AccountController {
    
    private final AccountValidator AccountValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final ProfileValidator profileValidator;
    private final PasswordValidator passwordValidator;
    private final EmailLogInValidator emailValidator;
    private final TeamRepository teamRepository;

    @InitBinder("accountForm")  //AccountForm요청이 들어올때 binder를 거치게된다.
    public void initBinderAccount(WebDataBinder webDataBinder){
        webDataBinder.addValidators(AccountValidator);
    }

    @InitBinder("profileForm")
    public void initBinderProfile(WebDataBinder webDataBinder){
        webDataBinder.addValidators(profileValidator);
    }

    @InitBinder("passwordForm")
    public void initBinderPassword(WebDataBinder webDataBinder){
        webDataBinder.addValidators(passwordValidator);
    }

    @InitBinder("emailLogInForm")
    public void initBinderemailLogIn(WebDataBinder webDataBinder){
        webDataBinder.addValidators(emailValidator);
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
        if(account == null || !account.getEmailCheckToken().equals(token)){
            model.addAttribute("error", "인증에 실패했습니다.");
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
        return "account/profile";
    }

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@Principal Account account, Model model){
        model.addAttribute("account", account);
        model.addAttribute("profileForm", new ProfileForm(account));
        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileUpdate(@Principal Account account, @Valid ProfileForm profileForm, Errors errors,
    Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            //에러는 자동으로담김
            model.addAttribute("account", account);
            return "settings/profile";
        }
        accountService.updateProfile(account, profileForm);
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        return "redirect:/settings/profile";
    }

    @GetMapping("/settings/password")
    public String passwordUpdateform(@Principal Account account, Model model){
        model.addAttribute("account", account);
        model.addAttribute("passwordForm", new PasswordForm());
        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String passwordUpdate(@Principal Account account, @Valid PasswordForm passwordForm, Errors errors,
    Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute("account", account);
            return "settings/password";
        }

        accountService.updatePassword(account, passwordForm);
        attributes.addFlashAttribute("message", "비밀번호를 변경했습니다.");
        return "redirect:/settings/password";
    }

    @PostMapping("/settings/sign-out")
    public String deleteAccount(@Principal Account account,
    Model model, RedirectAttributes attributes){
      
        accountService.deleteAccount(account);
        attributes.addFlashAttribute("message", "탈퇴했습니다.");
        return "redirect:/logout";
    }

    @GetMapping("/settings/sign-out")
    public String deleteAccount(@Principal Account account,
    Model model){
    
        model.addAttribute("account", account);
        return "settings/sign-out";
    }

    @GetMapping("/my-team")
    public String viewMyTeam(@Principal Account account, Model model){
        if(account != null){
            model.addAttribute("account", account);
            List<Team> memberList = teamRepository.findByMembers(account);
            List<Team> waittingList = teamRepository.findByWaitting(account);
            List<Team> managerList = teamRepository.findByManager(account);
            
            model.addAttribute("memberList", memberList);
            model.addAttribute("waittingList", waittingList);
            model.addAttribute("managerList", managerList);
        }
        return "account/my-team";
    }

    @GetMapping("/email-login")
    public String emailLogInForm(Model model){
        model.addAttribute("emailLogInForm", new EmailLogInForm());
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String emailLogIn(@Valid EmailLogInForm emailLogInForm, Errors errors, Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            return "account/email-login";
        }

        accountService.sendLogInLink(emailLogInForm.getEmail());
        attributes.addFlashAttribute("message", "이메일을 보냈습니다. 확인해주세요.");
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";

        if(account == null || !account.getEmailCheckToken().equals(token)){
            model.addAttribute("error", "인증에 실패했습니다.");
            return view;
        }

        accountService.login(account);      //이메일 확인 후 자동 로그인
        model.addAttribute("nickname", account.getNickname());

        return view;
    }
}
