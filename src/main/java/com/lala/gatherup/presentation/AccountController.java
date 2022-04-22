package com.lala.gatherup.presentation;


import javax.validation.Valid;

import com.lala.gatherup.application.SignUpApplicationService;
import com.lala.gatherup.domain.account.AccountService;
import com.lala.gatherup.infrastructure.config.Principal;
import com.lala.gatherup.domain.team.Team;
import com.lala.gatherup.domain.team.TeamRepository;
import com.lala.gatherup.domain.account.Account;
import com.lala.gatherup.domain.account.AccountRepository;
import com.lala.gatherup.presentation.dto.EmailLogInForm;
import com.lala.gatherup.presentation.dto.PasswordForm;
import com.lala.gatherup.presentation.dto.ProfileForm;
import com.lala.gatherup.presentation.dto.SignUpDto;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

// TODO 리팩토링 - view와 API를 동시에 사용합니다.
@Controller
@RequiredArgsConstructor
public class AccountController {

    // presentation 영역의 객체이므로 단반향 참조가 가능.
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TeamRepository teamRepository;
    private final SignUpApplicationService accountService;


    // 회원가입 get요청
    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm", new SignUpDto.Request());
        // 회원가입 요청 dto 객체를 보냅니다.
        return "account/sign-up";
    }

    // TODO initbinder 부분 -> handler로 변경
    // 검증해야할 다수의 객체가 있을땐 modelattribute
    // vaild 처리를 해줄 Errors 클래스.

    // 회원가입 post요청 TODO JSON 리턴
    // inline javascript를 사용해야 합니다. 객체를 json으로 자동 변환.
    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpDto.Response> signUpSubmitAndLogin(@RequestBody @Valid SignUpDto.Request singUpRequest, Errors errors) {
        if(errors.hasErrors()){
            // 오류가 있는 경우
            return ResponseEntity.badRequest()
                    .body()
        }

        var result = accountService.signUpAndLogin(singUpRequest);
        var response = convertToResponse(result);
        return ResponseEntity.ok().body(response);
        // 정상적인 경우 dto 객체를 뷰로 리턴.
    }

    @ResponseBody
    @PostMapping("/sign-up")
    public SignUpDto.Response signUpSubmitAndLogin(@RequestBody @Valid SignUpDto.Request singUpRequest) {
        var result = accountService.signUpAndLogin(singUpRequest);
        return convertToResponse(result);
        // 정상적인 경우 dto 객체를 뷰로 리턴.
    }

    /*
    이메일 인증 get요청
    */
    @GetMapping("/check-email-token")
    public String checkEmail(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if(account == null || !account.isValidToken(token)){
            model.addAttribute("error", "인증에 실패했습니다.");
            return view;
        }

        accountService.completeSignUp(account);      //이메일 확인 후 자동 로그인
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
        if(!account.isEnableSendEmail()){
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
    public GetAccountProfileResponse profileUpdateForm(@Principal Account account, Model model){
        model.addAttribute("account", account);
        model.addAttribute("profileForm", new ProfileForm(account));
        var response = convertAccountToResponse(account);
        return response;
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
