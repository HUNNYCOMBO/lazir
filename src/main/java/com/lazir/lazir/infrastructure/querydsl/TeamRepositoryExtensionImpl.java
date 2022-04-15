package com.lazir.lazir.infrastructure.querydsl;

import java.util.List;

import com.lazir.lazir.domain.team.QTeam;
import com.lazir.lazir.domain.team.Team;
import com.lazir.lazir.domain.team.TeamRepositoryExtension;
import com.querydsl.jpa.JPQLQuery;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class TeamRepositoryExtensionImpl extends QuerydslRepositorySupport implements TeamRepositoryExtension {

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
