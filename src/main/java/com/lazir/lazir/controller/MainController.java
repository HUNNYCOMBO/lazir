package com.lazir.lazir.controller;

import com.lazir.lazir.config.Principal;
import com.lazir.lazir.domain.Account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    
    @GetMapping("/")
    public String home(@Principal Account account, Model model) {
        if(account != null){
            model.addAttribute("account", account);
        }
        return "index";
    }
    
    @GetMapping("/login")
    public String logIn(){
        return "login";
    }

}
