package com.kob.backend.controller.user.bot;

import com.kob.backend.config.UnifyResponse;
import com.kob.backend.dto.UpdateBotDTO;
import com.kob.backend.exception.Ok;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/bot/")
@Validated
public class UpdateController {
    @Autowired
    private UpdateService updateService;

    @PostMapping("update/")
    public Ok update(@RequestBody @Validated UpdateBotDTO updateBotDTO){
        updateService.update(
                updateBotDTO.getBotId(),
                updateBotDTO.getTitle(),
                updateBotDTO.getDescription(),
                updateBotDTO.getContent());
        return UnifyResponse.ok();
    }
}
