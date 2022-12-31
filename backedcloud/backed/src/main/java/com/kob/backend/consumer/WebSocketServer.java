package com.kob.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.util.Game;
import com.kob.backend.entity.User;
import com.kob.backend.exception.http.ForbiddenException;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    public static final ConcurrentHashMap<Long, WebSocketServer> userSocketMap = new ConcurrentHashMap<>();
    private User user;
    private Session session;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    public static RestTemplate restTemplate;
    private Game game;

    private static final String addPlayerUrl = "http://127.0.0.1:5001/player/add/";
    private static final String removePlayerUrl = "http://127.0.0.1:5001/player/remove/";

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        System.out.println("open!");
        this.session = session;

        long userId = JwtUtil.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user == null) {
            throw new ForbiddenException(1003);
        }
        userSocketMap.put(this.user.getId(), this);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("close!");
        if (this.user != null) {
            userSocketMap.remove(this.user.getId());
        }
    }

    public static void startGame(Long aId, Long bId) {
        User userA = userMapper.selectById(aId);
        User userB = userMapper.selectById(bId);
        Game game = new Game(13, 14, 20, userA.getId(), userB.getId());
        game.createMap();
        if (userSocketMap.get(userA.getId()) != null) {
            userSocketMap.get(userA.getId()).game = game;
        }
        if (userSocketMap.get(userB.getId()) != null) {
            userSocketMap.get(userB.getId()).game = game;
        }
        game.start();

        JSONObject gameResp = new JSONObject();
        gameResp.put("map", game.getG());
        gameResp.put("a_id", userA.getId());
        gameResp.put("a_sx", game.getPlayerA().getSx());
        gameResp.put("a_sy", game.getPlayerA().getSy());
        gameResp.put("b_id", userB.getId());
        gameResp.put("b_sx", game.getPlayerA().getSx());
        gameResp.put("b_sy", game.getPlayerA().getSy());

        JSONObject resA = new JSONObject();
        resA.put("event", "match-success");
        resA.put("opponent_photo", userB.getPhoto());
        resA.put("opponent_username", userB.getUsername());
        resA.put("game", gameResp);
        if (userSocketMap.get(userA.getId()) != null) {
            userSocketMap.get(userA.getId()).sendMessage(resA.toJSONString());
        }

        JSONObject resB = new JSONObject();
        resB.put("event", "match-success");
        resB.put("opponent_photo", userA.getPhoto());
        resB.put("opponent_username", userA.getUsername());
        resB.put("game", gameResp);
        if (userSocketMap.get(userB.getId()) != null) {
            userSocketMap.get(userB.getId()).sendMessage(resB.toJSONString());
        }
    }

    private void startMatching(){
        System.out.println("start matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMatching(){
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(Integer direction) {
        if (game.getPlayerA().getUserId().equals(user.getId())){
            game.setNextStepA(direction);
        }else if (game.getPlayerB().getUserId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println(message);
        JSONObject data = JSON.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)){
            startMatching();
        } else if ("stop-matching".equals(event)){
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized(this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

