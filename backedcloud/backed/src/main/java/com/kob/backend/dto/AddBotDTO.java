package com.kob.backend.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddBotDTO {
    @NotBlank
    @Length(max = 100)
    private String title;

    @NotNull
    @Length(max = 300)
    private String description;

    @NotNull
    @Length(max = 10000)
    private String content;
}
