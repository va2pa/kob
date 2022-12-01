package com.kob.backend.controller.user.account;

import com.kob.backend.config.UnifyResponse;
import com.kob.backend.exception.Ok;
import com.kob.backend.exception.http.ParameterException;
import com.kob.backend.service.user.account.RegisterService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/account/")
@Validated
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("register/")
    public Ok register(@RequestParam @Length(min = 1, max = 100) String username,
                       @RequestParam @Length(min = 1, max = 100) String password,
                       @RequestParam @Length(min = 1, max = 100) String confirmPassword){
        if (username.trim().length() == 0){
            throw new ParameterException(1000);
        }
        if (!password.equals(confirmPassword)){
            throw new ParameterException(1001);
        }
        registerService.register(username, password);
        return UnifyResponse.ok();
    }
}
