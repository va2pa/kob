package com.kob.backend.exception;

import com.kob.backend.exception.http.HttpException;

public class Ok extends HttpException {
    public Ok(){
        this.code = 0;
        this.httpStatusCode = 200;
    }
}
