package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    @Override
    public String addPlayer(Long userId, Integer rating) {
        System.out.println("add player: " + userId);
        return "add player success";
    }

    @Override
    public String removePlayer(Long userId) {
        System.out.println("remove player: " + userId);
        return "remove player success";
    }
}
