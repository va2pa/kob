package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {

    @Override
    public void startGame(Long aId, Long bId) {
        System.out.println("start game " + aId + ", " + bId);
        WebSocketServer.startGame(aId, bId);
    }
}
