package com.lazir.lazir.repository;

import java.util.List;

import com.lazir.lazir.domain.QTeam;
import com.lazir.lazir.domain.Team;
import com.querydsl.jpa.JPQLQuery;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class TeamRepositoryExtensionImpl extends QuerydslRepositorySupport implements TeamRepositoryExtension{

    public TeamRepositoryExtensionImpl() {
        super(Team.class);
    }

    @Override
    public List<Team> findByKeyword(String keyword) {
        QTeam team = QTeam.team;
        JPQLQuery<Team> query = from(team).where(team.published.isTrue()
                .and(team.name.containsIgnoreCase(keyword))
                .or(team.title.containsIgnoreCase(keyword))
                .or(team.context.containsIgnoreCase(keyword)));
        return query.fetch();
    }
    
}
