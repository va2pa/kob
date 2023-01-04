package com.kob.backend.controller.pk;

import com.kob.backend.config.UnifyResponse;
import com.kob.backend.exception.Ok;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/pk/")
public class StartGameController {
    @Autowired
    private StartGameService startGameService;

    @PostMapping("start/game/")
    public Ok startGame(@RequestParam MultiValueMap<String, String> data) {
        long aId = Long.parseLong(Objects.requireNonNull(data.getFirst("a_id")));
        long aBotId = Long.parseLong(Objects.requireNonNull(data.getFirst("a_bot_id")));
        long bId = Long.parseLong(Objects.requireNonNull(data.getFirst("b_id")));
        long bBotId = Long.parseLong(Objects.requireNonNull(data.getFirst("b_bot_id")));
        startGameService.startGame(aId, aBotId, bId, bBotId);
        return UnifyResponse.ok();
    }
}
