package com.lala.gatherup.controller;

import java.util.List;

import com.lala.gatherup.config.Principal;
import com.lala.gatherup.domain.Account;
import com.lala.gatherup.domain.Team;
import com.lala.gatherup.repository.TeamRepository;

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
