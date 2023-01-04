package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.util.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRuningServiceImpl implements BotRunningService {
    public static BotPool botPool = new BotPool();
    @Override
    public void addBot(Long userId, String botCode, String input) {
        System.out.println("addBot" + userId + " " + botCode + " " + input);
        botPool.addBot(userId, botCode, input);
    }
}
