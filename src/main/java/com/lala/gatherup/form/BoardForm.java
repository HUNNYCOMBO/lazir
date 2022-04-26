package com.lala.gatherup.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class BoardForm {
    
    @NotNull
    @NotBlank
    @Length(min = 1, max = 100)
    private String title;

    private String content;
}