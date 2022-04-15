package com.lazir.lazir.domain.team;

import java.util.List;

import com.lazir.lazir.domain.team.Team;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TeamRepositoryExtension {
    
    List<Team> findByKeyword(String keyword);
}
