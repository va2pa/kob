package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.util.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public void receiveBotMove(Long userId, int direction) {
        WebSocketServer webSocketServer = WebSocketServer.userSocketMap.get(userId);
        Game game = webSocketServer.game;
        if (game.getPlayerA().getUserId().equals(userId)){
            game.setNextStepA(direction);
        }else if (game.getPlayerB().getUserId().equals(userId)){
            game.setNextStepB(direction);
        }
    }
}
