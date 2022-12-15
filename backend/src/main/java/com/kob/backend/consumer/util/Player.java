package com.kob.backend.consumer.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Long userId;
    private Integer sx;
    private Integer sy;
    private ArrayList<Integer> stepList;
}
