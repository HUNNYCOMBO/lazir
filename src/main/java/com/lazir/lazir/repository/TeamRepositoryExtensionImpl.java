package com.lazir.lazir.repository;

import java.util.List;

import com.lazir.lazir.domain.Team;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class TeamRepositoryExtensionImpl extends QuerydslRepositorySupport implements TeamRepositoryExtension{

    public TeamRepositoryExtensionImpl() {
        super(Team.class);
    }

    @Override
    public List<Team> findByKeyword(String keyword) {
        
        return null;
    }
    
}
