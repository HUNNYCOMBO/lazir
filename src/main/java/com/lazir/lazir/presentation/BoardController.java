package com.lazir.lazir.presentation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lazir.lazir.infrastructure.config.Principal;
import com.lazir.lazir.domain.account.Account;
import com.lazir.lazir.domain.board.Board;
import com.lazir.lazir.domain.board.BoardRepository;
import com.lazir.lazir.domain.board.BoardService;
import com.lazir.lazir.presentation.dto.BoardForm;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;
   
    private final BoardRepository boardRepository;
   

    @GetMapping("/write-board")
    public String writeBoardForm(Model model, @Principal Account account){
        model.addAttribute("boardForm", new BoardForm());
        model.addAttribute(account);
        return "board/write-board";
    }

    @PostMapping("/write-board")
    public String writeBoardSubmit(@Principal Account account, BoardForm board){
        boardService.crateNewBoard(board, account);
        return "redirect:/board/list";
    }

    @GetMapping("/board/{id}")
    public String viewBoard(Model model, @PathVariable Long id, @Principal Account account){
        model.addAttribute("board", boardService.viewBoardById(id));
        model.addAttribute("account",account);
        return "board/board-detail";
    }
     
    @GetMapping("board/{id}/updateForm")
	public String updateForm(@PathVariable Long id,Model model) {
		model.addAttribute("board",boardService.viewBoardById(id));
		return "board/updateForm";
	}
	
    
    @GetMapping("board/list")
    public String viewBoardList(Model model, @PageableDefault(size=10,sort="id",direction = Sort.Direction.DESC)Pageable pageable, 
    		                    @RequestParam(required = false, defaultValue = "")String searchText){
       
    	//Page<Board> boards = boardService.boardList(pageable);
    	Page<Board> boards = boardService.boardList(pageable,searchText);
	
    	int startPage =(int) Math.floor(boards.getPageable().getPageNumber()/10)*10+1;
		int endPage = startPage + 9 <  boards.getTotalPages() ? startPage + 9 : boards.getTotalPages();
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("boards",boards);
      
        return "board/board-list";
    }
    
}
