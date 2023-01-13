package com.kob.backend.controller.user.bot;

import com.kob.backend.config.UnifyResponse;
import com.kob.backend.dto.AddBotDTO;
import com.kob.backend.exception.Ok;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/bot/")
@Validated
public class AddController {
    @Autowired
    private AddService addService;

    @PostMapping("add/")
    public Ok add(@RequestBody @Validated AddBotDTO addBotDTO){
        addService.add(addBotDTO.getTitle(),
                addBotDTO.getDescription(),
                addBotDTO.getContent());
        return UnifyResponse.ok();
    }
}
