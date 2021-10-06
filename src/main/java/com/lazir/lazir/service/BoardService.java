// package com.lazir.lazir.service;

// import java.util.List;

// import javax.validation.Valid;

// import com.lazir.lazir.domain.Account;
// import com.lazir.lazir.domain.Board;
// import com.lazir.lazir.form.BoardForm;
// import com.lazir.lazir.repository.BoardRepository;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// @Transactional
// public class BoardService {
    
//     private final BoardRepository boardRepository;

//     public Board crateNewBoard(@Valid BoardForm boardForm, Account account){
//         Board newBoard = Board.builder()
//         .title(boardForm.getTitle())
//         .content(boardForm.getContent())
//         .count((long) 0)
//         .account(account)
//         .build();
//         return boardRepository.save(newBoard);
//     }

//     public Board viewBoardById(Long id){
//         return boardRepository.findById(id)
//         .orElseThrow(()->{return new IllegalArgumentException("글 조회 실패");});
//     }

//     public List<Board> boardList(){
//         return boardRepository.findAll();
//     }
// }
