package com.kob.backend.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class BasePageDTO {
    @Min(value = 1)
    @Max(value = 30)
    private Integer count;

    @Min(value = 0)
    private Integer page;

    public Integer getCount() {
        if (null == count) {
            return 10;
        }
        return count;
    }

    public Integer getPage() {
        if (null == page) {
            return 0;
        }
        return page;
    }

}
