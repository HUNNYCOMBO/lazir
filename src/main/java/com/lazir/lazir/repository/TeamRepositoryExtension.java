package com.lazir.lazir.repository;

import java.util.List;

import com.lazir.lazir.domain.Team;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TeamRepositoryExtension {
    
    List<Team> findByKeyword(String keyword);
}
