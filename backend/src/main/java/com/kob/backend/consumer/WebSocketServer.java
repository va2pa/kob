package com.kob.backend.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.util.Game;
import com.kob.backend.entity.User;
import com.kob.backend.exception.http.ForbiddenException;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private static final CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session;
    private static UserMapper userMapper;

    private Game game;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
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
            matchpool.remove(this.user);
        }
    }

    private void startMatching(){
        System.out.println("start matching");
        matchpool.add(this.user);

        while(matchpool.size() >= 2){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Iterator<User> iterator = matchpool.iterator();
            User userA = iterator.next();
            User userB = iterator.next();
            matchpool.remove(userA);
            matchpool.remove(userB);

            Game game = new Game(13, 14, 20, userA.getId(), userB.getId());
            game.createMap();
            userSocketMap.get(userA.getId()).game = game;
            userSocketMap.get(userB.getId()).game = game;
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
            userSocketMap.get(userA.getId()).sendMessage(resA.toJSONString());

            JSONObject resB = new JSONObject();
            resB.put("event", "match-success");
            resB.put("opponent_photo", userA.getPhoto());
            resB.put("opponent_username", userA.getUsername());
            resB.put("game", gameResp);
            userSocketMap.get(userB.getId()).sendMessage(resB.toJSONString());
        }
    }

    private void stopMatching(){
        System.out.println("stop matching");
        matchpool.remove(this.user);
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

