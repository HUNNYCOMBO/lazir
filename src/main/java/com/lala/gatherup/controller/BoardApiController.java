package com.lala.gatherup.controller;

import com.lala.gatherup.config.PrincipalDetail;
import com.lala.gatherup.domain.Board;
import com.lala.gatherup.domain.Reply;
import com.lala.gatherup.domain.ResponseDto;
import com.lala.gatherup.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardApiController {
   
	
	@Autowired
	private BoardService boardService;
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable Long id){
		boardService.boardDelete(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
	}
	
	@PutMapping("/api/board/{id}")
    public ResponseDto<Integer> updateBoard(@PathVariable Long id,@RequestBody Board board){
         boardService.insertBoard(id,board);
         return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
	
    @PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> saveReply(@PathVariable long boardId, @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal) {
    	boardService.writeReply(principal.getAccount(),boardId,reply);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
	}
   
    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> deleteReplyId(@PathVariable long replyId) { 
    	boardService.deleteReply(replyId);
    	return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
    }
    
}
