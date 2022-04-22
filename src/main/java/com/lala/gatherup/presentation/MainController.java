package com.lala.gatherup.presentation;

import java.util.List;

import com.lala.gatherup.infrastructure.config.Principal;
import com.lala.gatherup.domain.team.Team;
import com.lala.gatherup.domain.team.TeamRepository;
import com.lala.gatherup.domain.account.Account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {
    
    private final TeamRepository teamRepository;

    @GetMapping("/")
    public String home(@Principal Account account, Model model) {
        if(account != null){
            model.addAttribute("account", account);
        }
        model.addAttribute("teamList", teamRepository.findFirst12ByPublishedAndClosedOrderByCreateTimeDesc(true, false));
        return "index";
    }
    
    @GetMapping("/login")
    public String logIn(){
        return "login";
    }

    @GetMapping("/search/team")
    public String searchTeam(String keyword, Model model){
        List<Team> teamList = teamRepository.findByKeyword(keyword);
        model.addAttribute("teamList", teamList);
        model.addAttribute("keyword", keyword);
        return "search";
    }
}
