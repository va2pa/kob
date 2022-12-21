package com.kob.backend.config;

import com.kob.backend.exception.Ok;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnifyResponse {
    private int code;
    private String message;
    private String requestURL;

    public static Ok ok(){
        throw new Ok();
    }
}
