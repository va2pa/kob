package com.kob.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseVO<T> {

    private Long total;

    private List<T> items;

    private Integer page;

    private Integer count;
}
