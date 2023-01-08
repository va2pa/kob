package com.kob.backend.entity;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bot extends BaseEntity{
    private Long userId;

    private String title;

    private String description;

    private String content;
}
