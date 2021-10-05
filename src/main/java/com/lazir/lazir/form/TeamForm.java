package com.lazir.lazir.form;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class TeamForm {
    
    @NotBlank
    @NotNull
    @Length(min = 3, max = 40)
    private String title;

    @NotBlank
    @NotNull
    @Length(min = 2,max = 20)
    private String name;

    @NotBlank
    private String context;

    private Integer memberLimit;

    @NotNull
    private boolean published;

    private String URL;

    public TeamForm(){
        this.URL = UUID.randomUUID().toString();
    }
}
