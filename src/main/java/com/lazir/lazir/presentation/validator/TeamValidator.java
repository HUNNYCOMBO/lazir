package com.lazir.lazir.presentation.validator;


import com.lazir.lazir.domain.team.TeamRepository;
import com.lazir.lazir.presentation.dto.TeamForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeamValidator implements Validator{
    
    private final TeamRepository teamRepository;

    @Override
    public boolean supports(Class<?> class1) {
       return class1.isAssignableFrom(TeamForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        TeamForm teamForm = (TeamForm)object;
        if(teamRepository.existsByURL(teamForm.getURL())){
            errors.rejectValue("URL", "일시적 에러 입니다. 다시 시도해주세요.");
        }
  
    }


}
