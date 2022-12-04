package com.kob.backend.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UpdateBotDTO {
    @NotNull
    private Long botId;

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
