package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {

    @Override
    public void startGame(Long aId, Long aBotId, Long bId, Long bBotId) {
        System.out.println("start game " + aId + " " + aBotId + ", "  + bId + " " + bBotId);
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
    }
}
