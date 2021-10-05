package com.lazir.lazir.service;

import java.time.LocalDateTime;

import javax.validation.Valid;

import com.lazir.lazir.domain.Account;
import com.lazir.lazir.domain.Team;
import com.lazir.lazir.form.TeamForm;
import com.lazir.lazir.form.TeamModifyForm;
import com.lazir.lazir.repository.TeamRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {
    
    private final TeamRepository repository;

    public Team createNewTeam(Account account, TeamForm form) {
        Team team = Team.builder()
            .name(form.getName())
            .title(form.getTitle())
            .context(form.getContext())
            .manager(account)
            .recruiting(true)
            .recruitUpdateTime(LocalDateTime.now())
            .URL(form.getURL())
            .published(!form.isPublished())
            .createTime(LocalDateTime.now())
            .memberLimit(form.getMemberLimit())
            .build();
        
        return repository.save(team);
    }

    public void modifyTeam(Account account, @Valid TeamModifyForm teamModifyForm) {
        Team team = repository.findByURL(teamModifyForm.getURL());
        team.setName(teamModifyForm.getName());
        team.setTitle(teamModifyForm.getTitle());
        team.setContext(teamModifyForm.getContext());
        team.setMemberLimit(teamModifyForm.getMemberLimit());
        repository.save(team);
    }

    public void addWatting(Account account, Team team) {
        team.getWaitting().add(account);
        repository.save(team);
    }

    public void addMember(Account waitter, Team team) {
        team.getWaitting().remove(waitter);
        team.getMembers().add(waitter);
        if((team.getMembers().size()+1) == team.getMemberLimit()){
            team.setRecruiting(false);
        }
        repository.save(team);
    }

    public void removeWaitter(Account waitter, Team team) {
        team.getWaitting().remove(waitter);
        repository.save(team);
    }

    public void removeMember(Account account, Team team) {
        if(team.getWaitting().contains(account)){
            team.getWaitting().remove(account);
        }
        team.getMembers().remove(account);
        repository.save(team);
    }

    public void transferManager(Account member, Team team, Account account) {
        team.setManager(member);
        team.getMembers().remove(member);
        team.getMembers().add(account);
        repository.save(team);
    }

    public void startPublish(Team team) {
        team.setPublished(true);
        repository.save(team);
    }

    public void closeTeam(Team team) {
        team.setClosed(true);
        team.setClosedTime(LocalDateTime.now());
        repository.save(team);
    }

    public void startRecruit(Team team) {
        team.setRecruiting(true);
        team.setRecruitUpdateTime(LocalDateTime.now());
        repository.save(team);
    }

    public void stopRecruit(Team team) {
        team.setRecruiting(false);
        team.setRecruitUpdateTime(LocalDateTime.now());
        repository.save(team);
    }

    public void remove(Team team) {
        repository.delete(team);
    }
    
}
