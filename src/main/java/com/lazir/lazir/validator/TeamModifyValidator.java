package com.lazir.lazir.validator;


import com.lazir.lazir.domain.Team;
import com.lazir.lazir.form.TeamModifyForm;
import com.lazir.lazir.repository.TeamRepository;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamModifyValidator implements Validator{
    
    private final TeamRepository teamRepository;

    @Override
    public boolean supports(Class<?> class1) {
       return class1.isAssignableFrom(TeamModifyForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        TeamModifyForm teamModifyForm = (TeamModifyForm)object;
        Team team = teamRepository.findByURL(teamModifyForm.getURL());
        log.info("로그 : " + teamModifyForm + "팀" + team);
        if(teamModifyForm.getMemberLimit() < (team.getMembers().size()+1)){
            errors.rejectValue("memberLimit", "현재 인원보다 작게 설정할 수 없습니다.");
        }
    }


}
