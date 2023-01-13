package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/account/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("token/")
    public Map<String, String> getToken(@RequestParam String username,
                                        @RequestParam String password){
        String token = loginService.getToken(username, password);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
