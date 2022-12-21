package com.kob.backend.controller.user.account;

import com.kob.backend.entity.User;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/account/")
public class InfoController {
    @Autowired
    private InfoService infoService;


    @GetMapping("info/")
    public User getInfo(){
        return infoService.getInfo();
    }
}
