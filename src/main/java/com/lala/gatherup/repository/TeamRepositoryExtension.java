package com.lala.gatherup.repository;

import java.util.List;

import com.lala.gatherup.domain.Team;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TeamRepositoryExtension {
    
    List<Team> findByKeyword(String keyword);
}
