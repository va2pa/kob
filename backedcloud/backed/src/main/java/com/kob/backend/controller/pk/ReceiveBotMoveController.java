package com.kob.backend.controller.pk;

import com.kob.backend.config.UnifyResponse;
import com.kob.backend.exception.Ok;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/pk/")
public class ReceiveBotMoveController {
    @Autowired
    private ReceiveBotMoveService receiveBotMoveService;

    @PostMapping("receive/bot/move/")
    public Ok receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Long userId = Long.parseLong(Objects.requireNonNull(data.getFirst("user_id")));
        Integer dircetion = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        receiveBotMoveService.receiveBotMove(userId, dircetion);
        return UnifyResponse.ok();
    }
}
