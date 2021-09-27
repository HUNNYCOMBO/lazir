package com.lazir.lazir.controller;

import com.lazir.lazir.config.Principal;
import com.lazir.lazir.domain.Account;
import com.lazir.lazir.form.BoardForm;
import com.lazir.lazir.service.BoardService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;

    @GetMapping("/write-board")
    public String writeBoardForm(Model model, @Principal Account account){
        model.addAttribute("boardForm", new BoardForm());
        model.addAttribute(account);
        return "board/write-board";
    }

    @PostMapping("/write-board")
    public String writeBoardSubmit(@Principal Account account, BoardForm board){
        boardService.crateNewBoard(board, account);
        return "redirect:/";
    }

    @GetMapping("/board/{id}")
    public String viewBoard(Model model, @PathVariable Long id){
        model.addAttribute("boardview", boardService.viewBoardById(id));
        return "board/board-detail";
    }

    @GetMapping("/board")
    public String viewBoardList(Model model){
        model.addAttribute("boards", boardService.boardList());
        return "/board/board-list";
    }
}
