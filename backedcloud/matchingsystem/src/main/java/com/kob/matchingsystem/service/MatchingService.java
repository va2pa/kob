package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Long userId, Long rating, Long botId);
    String removePlayer(Long userId);
}
