package com.lala.gatherup.presentation.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EmailLogInForm {
    
    @NotNull
    @Email
    private String email;

}
