package com.lala.gatherup.service;

import javax.validation.Valid;

import com.lala.gatherup.domain.Account;
import com.lala.gatherup.domain.Board;
import com.lala.gatherup.domain.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.lala.gatherup.form.BoardForm;
import com.lala.gatherup.repository.BoardRepository;
import com.lala.gatherup.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    
	@Autowired
    private final BoardRepository boardRepository;
    
    @Autowired
    private ReplyRepository replyRepository;
   
    
    public Board crateNewBoard(@Valid BoardForm boardForm, Account account){
        Board newBoard = Board.builder()
        .title(boardForm.getTitle())
        .content(boardForm.getContent())
        .count((long) 0)
        .account(account)
        .build();
        return boardRepository.save(newBoard);
    }
   
    public Board viewBoardById(Long id){
    	Board board = boardRepository.findById(id).orElseThrow(() -> {
   			return new IllegalArgumentException("글 찾기 실패 : 아이디를  찾을 수 없습니다.");
   		});  
    	board.setCount(board.getCount() + 1);
        return boardRepository.findById(id)
        .orElseThrow(()->{return new IllegalArgumentException("글 조회 실패");});
    }

   	public void insertBoard(Long id, Board requestBoard) {
   		Board board = boardRepository.findById(id).orElseThrow(() -> {
   			return new IllegalArgumentException("글 찾기 실패 : 아이디를  찾을 수 없습니다.");
   		});  
          board.setTitle(requestBoard.getTitle());
          board.setContent(requestBoard.getContent());
   	}
    
	public void boardDelete(Long id) {
		boardRepository.deleteById(id);
	}
    
	
    public Page<Board> boardList(Pageable pageable,@RequestParam(required = false, defaultValue = "")String searchText){
    	return  boardRepository.findByTitleContainingOrContentContaining(searchText,searchText,pageable);
    }
    
    public void writeReply(Account account,Long boardId, Reply requestReply) {
    	
    	Board board = boardRepository.findById(boardId).orElseThrow(() -> {
   			return new IllegalArgumentException("댓글 쓰기  실패 : 게시글의 번호를  찾을 수 없습니다.");
   		});  
    	
    	requestReply.setAccount(account);
    	requestReply.setBoard(board);
    	
    	replyRepository.save(requestReply);
    }
    
    public void deleteReply(Long replyId) {
    	replyRepository.deleteById(replyId);
    }
  
}
