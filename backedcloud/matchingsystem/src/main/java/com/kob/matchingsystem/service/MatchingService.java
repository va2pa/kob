package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Long userId, Long rating);
    String removePlayer(Long userId);
}
