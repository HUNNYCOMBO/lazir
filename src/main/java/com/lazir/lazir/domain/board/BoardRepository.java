package com.lazir.lazir.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
    
	Page<Board> findByTitleContainingOrContentContaining(String title,String content,Pageable pageable);
	
	Page<Board> findByTitleContaining(String title,Pageable pageable);
	
	Page<Board> findByContentContaining(String title,Pageable pageable);
}
