package com.lazir.lazir.repository;

import com.lazir.lazir.domain.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BoardRepository extends JpaRepository<Board, Long>{
    
}
