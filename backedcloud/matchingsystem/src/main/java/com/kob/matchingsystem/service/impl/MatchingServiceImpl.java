package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.util.MatchPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {

    public static MatchPoolService matchPoolService = new MatchPoolService();
    @Override
    public String addPlayer(Long userId, Long rating, Long botId) {
        System.out.println("add player: " + userId + " " + botId);
        matchPoolService.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Long userId) {
        System.out.println("remove player: " + userId);
        matchPoolService.removePlayer(userId);
        return "remove player success";
    }
}
