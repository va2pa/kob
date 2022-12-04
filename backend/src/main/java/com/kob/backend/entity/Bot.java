package com.kob.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bot extends BaseEntity{
    private Long userId;

    private String title;

    private String description;

    private String content;

    private Long rating;
}
