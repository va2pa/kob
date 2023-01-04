package com.kob.matchingsystem.service.impl.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Long userId;
    private Long rating;
    private Long botId;
    private Integer waitingTime;
}
