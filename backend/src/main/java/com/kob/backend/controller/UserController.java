package com.kob.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/")
public class UserController {

    @GetMapping("info/")
    public Map<String, String> getUserInfo(){
        Map<String, String> res = new HashMap<>();
        res.put("username", "wide");
        res.put("rating", "99");
        return res;
    }
}
