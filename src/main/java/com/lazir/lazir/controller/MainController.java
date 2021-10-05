package com.lazir.lazir.controller;

import com.lazir.lazir.config.Principal;
import com.lazir.lazir.domain.Account;
import com.lazir.lazir.repository.TeamRepository;

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
        model.addAttribute("teamList", teamRepository.findAll());
        return "index";
    }
    
    @GetMapping("/login")
    public String logIn(){
        return "login";
    }

}
