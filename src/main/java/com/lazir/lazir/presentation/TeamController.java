package com.lazir.lazir.presentation;

import java.net.URLEncoder;

import javax.validation.Valid;

import com.lazir.lazir.infrastructure.config.Principal;
import com.lazir.lazir.domain.team.Team;
import com.lazir.lazir.domain.team.TeamRepository;
import com.lazir.lazir.domain.team.TeamService;
import com.lazir.lazir.domain.account.Account;
import com.lazir.lazir.domain.account.AccountRepository;
import com.lazir.lazir.presentation.dto.TeamForm;
import com.lazir.lazir.presentation.dto.TeamModifyForm;
import com.lazir.lazir.presentation.validator.TeamModifyValidator;
import com.lazir.lazir.presentation.validator.TeamValidator;
import com.nimbusds.jose.util.StandardCharset;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;
    private final TeamRepository repository;
    private final TeamValidator teamValidator;
    private final TeamModifyValidator teamModifyValidator;
    private final AccountRepository accountRepository;

    @InitBinder("teamForm")
    public void initBinderTeam(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(teamValidator);
    }

    @InitBinder("teamModifyForm")
    public void initBinderModifyTeam(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(teamModifyValidator);
    }

    @GetMapping("/create-team")
    public String crateTeamForm(@Principal Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("teamForm", new TeamForm());
        return "team/create-team";
    }

    @PostMapping("/create-team")
    public String createTeam(@Principal Account account, @Valid TeamForm teamForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "team/create-team";
        }
        Team team = service.createNewTeam(account, teamForm);
        return "redirect:/team/" + URLEncoder.encode(team.getURL(), StandardCharset.UTF_8);
    }

    @GetMapping("/team/{url}")
    public String viewTeam(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        if (team == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        model.addAttribute("account", account);
        model.addAttribute("team", team);
        return "team/view";
    }

    @GetMapping("/team/{url}/members")
    public String memberTeam(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        if (team == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        model.addAttribute("account", account);
        model.addAttribute("team", team);
        return "team/members";
    }

    @GetMapping("/team/{url}/settings/modify")
    public String settingTeamForm(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        Account owner = accountRepository.findByEmail(account.getEmail());
        if (team == null || !team.getManager().equals(owner)) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        model.addAttribute("teamModifyForm", new TeamModifyForm());
        model.addAttribute("account", account);
        model.addAttribute("team", team);
        return "team/settings/modify";
    }

    @PostMapping("/team/{url}/settings/modify")
    public String settingTeam(@Principal Account account, @PathVariable String url,
            @Valid TeamModifyForm teamModifyForm, Errors errors, Model model, RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        if (team == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            return "team/" + url + "/settings/modify";
        }

        service.modifyTeam(account, teamModifyForm);
        attributes.addFlashAttribute("message", "팀 정보를 변경하였습니다.");
        return "redirect:/team/" + url + "/settings/modify";
    }

    @PostMapping("/team/{url}/join")
    public String joinTeam(@Principal Account account, @PathVariable String url, Model model,
            RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        if (team == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        service.addWatting(account, team);
        attributes.addFlashAttribute("message", "참가신청을 보냈습니다.");
        return "redirect:/team/" + url;
    }

    @PostMapping("/team/{url}/leave")
    public String leaveTeam(@Principal Account account, @PathVariable String url, Model model,
            RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        if (team == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        service.removeMember(account, team);
        attributes.addFlashAttribute("message", "팀을 떠났습니다.");
        return "redirect:/team/" + url;
    }

    @PostMapping("/team/{url}/cancel")
    public String joinCancel(@Principal Account account, @PathVariable String url, Model model,
            RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        if (team == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        service.removeWaitter(account, team);
        attributes.addFlashAttribute("message", "신청을 취소했습니다.");
        return "redirect:/team/" + url;
    }

    @PostMapping("/team/{url}/admit/{nickname}")
    public String joinTeamAdmit(@Principal Account account, @PathVariable String url, @PathVariable String nickname,
            Model model, RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        Account waitter = accountRepository.findByNickname(nickname);
        if (team == null || waitter == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        if (!team.isAcceptable()) {
            attributes.addFlashAttribute("message", "정원을 초과했습니다.");
            return "redirect:/team/" + url + "/settings/manage-member";
        }
        service.addMember(waitter, team);
        attributes.addFlashAttribute("message", waitter.getNickname() + "님이 팀에 합류했습니다.");
        return "redirect:/team/" + url + "/settings/manage-member";
    }

    @PostMapping("/team/{url}/deny/{nickname}")
    public String joinTeamDeny(@Principal Account account, @PathVariable String url, @PathVariable String nickname,
            Model model, RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        Account waitter = accountRepository.findByNickname(nickname);
        if (team == null || waitter == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }

        service.removeWaitter(waitter, team);
        attributes.addFlashAttribute("message", waitter.getNickname() + "님을 거절했습니다.");
        return "redirect:/team/" + url + "/settings/manage-member";
    }

    @PostMapping("/team/{url}/transfer/{nickname}")
    public String transferLeader(@Principal Account account, @PathVariable String url, @PathVariable String nickname,
            Model model, RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        Account member = accountRepository.findByNickname(nickname);
        if (team == null || member == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }

        service.transferManager(member, team, account);
        attributes.addFlashAttribute("message", member.getNickname() + "님에게 권한을 넘겼습니다.");
        return "redirect:/team/" + url + "/members";
    }

    @PostMapping("/team/{url}/kick/{nickname}")
    public String kickMember(@Principal Account account, @PathVariable String url, @PathVariable String nickname,
            Model model, RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        Account member = accountRepository.findByNickname(nickname);
        if (team == null || member == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }

        service.removeMember(member, team);
        attributes.addFlashAttribute("message", member.getNickname() + "님을 내쫓았습니다.");
        return "redirect:/team/" + url + "/settings/manage-member";
    }

    @GetMapping("/team/{url}/settings/manage-member")
    public String manageMember(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        Account owner = accountRepository.findByEmail(account.getEmail());
        if (team == null || !team.getManager().equals(owner)) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        model.addAttribute("account", account);
        model.addAttribute("team", team);
        return "team/settings/manage-member";
    }

    @GetMapping("/team/{url}/settings/status")
    public String modifyStatus(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        Account owner = accountRepository.findByEmail(account.getEmail());
        if (team == null || !team.getManager().equals(owner)) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        model.addAttribute("account", account);
        model.addAttribute("team", team);
        return "team/settings/status";
    }

    @PostMapping("/team/{url}/settings/team/publish")
    public String startPublish(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        Account owner = accountRepository.findByEmail(account.getEmail());
        if (team == null || !team.getManager().equals(owner)) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        service.startPublish(team);
        model.addAttribute("account", account);
        model.addAttribute("team", team);
        return "redirect:/team/" + url + "/settings/status";
    }

    @PostMapping("/team/{url}/settings/team/close")
    public String closeTeam(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        Account owner = accountRepository.findByEmail(account.getEmail());
        if (team == null || !team.getManager().equals(owner)) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
        service.closeTeam(team);
        model.addAttribute("account", account);
        model.addAttribute("team", team);
        return "redirect:/team/" + url + "/settings/status";
    }

    @PostMapping("/team/{url}/settings/recruit/start")
    public String startRecruit(@Principal Account account, @PathVariable String url, Model model,
            RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        if (!team.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/team/" + url + "/settings/status";
        }

        service.startRecruit(team);
        attributes.addFlashAttribute("message", "인원 모집을 시작합니다.");
        return "redirect:/team/" + url + "/settings/status";
    }

    @PostMapping("/team/{url}/settings/recruit/stop")
    public String stopRecruit(@Principal Account account, @PathVariable String url, Model model,
            RedirectAttributes attributes) {
        Team team = repository.findByURL(url);
        if (!team.canUpdateRecruiting()) {
            attributes.addFlashAttribute("message", "1시간 안에 인원 모집 설정을 여러번 변경할 수 없습니다.");
            return "redirect:/team/" + url + "/settings/status";
        }

        service.stopRecruit(team);
        attributes.addFlashAttribute("message", "인원 모집을 종료합니다.");
        return "redirect:/team/" + url + "/settings/status";
    }

    @PostMapping("/team/{url}/settings/team/remove")
    public String removeStudy(@Principal Account account, @PathVariable String url, Model model) {
        Team team = repository.findByURL(url);
        service.remove(team);
        return "redirect:/";
    }

}
