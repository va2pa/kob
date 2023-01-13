package com.kob.backend.controller.user.bot;

import com.kob.backend.config.UnifyResponse;
import com.kob.backend.exception.Ok;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/bot/")
public class RemoveController {
    @Autowired
    private RemoveService removeService;

    @PostMapping("remove/")
    public Ok remove(@RequestParam Long botId){
        removeService.remove(botId);
        return UnifyResponse.ok();
    }
}
