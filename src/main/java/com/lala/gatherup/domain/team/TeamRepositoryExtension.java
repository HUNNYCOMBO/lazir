package com.lala.gatherup.domain.team;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TeamRepositoryExtension {
    
    List<Team> findByKeyword(String keyword);
}
