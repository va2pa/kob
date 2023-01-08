package com.kob.matchingsystem.service.impl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchPoolService extends Thread{
    private static ArrayList<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();
    private static RestTemplate restTemplate;
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchPoolService.restTemplate = restTemplate;
    }

    private final static String startGameUrl = "http://127.0.0.1:5000/pk/start/game/";

    public void addPlayer(Long userId, Long rating, Long botId) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, botId, 0));
        }finally {
            lock.unlock();
        }
    }
    public void removePlayer(Long userId) {
        lock.lock();
        try {
            ArrayList<Player> newPlayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        }finally {
            lock.unlock();
        }
    }

    private void increateWaitingTime() {
        for (Player player: players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private boolean checkMatched(Player a, Player b) {
        long rating = Math.abs(a.getRating() - b.getRating());
        int watingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return rating <= watingTime * 10;
    }

    private void matchPlayers() {
        System.out.println(players);
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i ++) {
            if (used[i]) {
                continue;
            }
            for (int j = i + 1; j < players.size(); j ++) {
                if (used[j]) {
                    continue;
                }
                Player a = players.get(i);
                Player b = players.get(j);
                if (checkMatched(a, b)) {
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        ArrayList<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i ++) {
            if (!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;

    }

    private void sendResult(Player a, Player b) {
        System.out.println("sendResult " + a + "," + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increateWaitingTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
