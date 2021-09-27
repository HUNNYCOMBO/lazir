package com.lazir.lazir.form;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class BoardForm {
    
    @NotBlank
    @Length(min = 1, max = 100)
    private String title;

    private String content;
}