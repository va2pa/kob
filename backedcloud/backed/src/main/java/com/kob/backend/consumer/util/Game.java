package com.kob.backend.consumer.util;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.entity.Bot;
import com.kob.backend.entity.Record;
import com.kob.backend.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

@Data
@NoArgsConstructor
public class Game extends Thread{
    private int[][] g;
    private Integer rows;
    private Integer cols;
    private int innerWallsCount;
    private static int[] dx = {0, 1, 0, -1};
    private static int[] dy = {1, 0, -1, 0};
    private Player playerA;
    private Player playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";
    private String loser = "";

    private final static String addBotUrl = "http://127.0.0.1:5002/bot/add/";

    public void setNextStepA(Integer nextStepA){
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        }finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        }finally {
            lock.unlock();
        }
    }

    public Game(int r, int c, int innerWallsCount, Long userIdA, Bot botA, Long userIdB, Bot botB){
        this.rows = r;
        this.cols = c;
        this.innerWallsCount = innerWallsCount;
        this.g = new int[r][c];
        Long botIdA = -1L;
        Long botIdB = -1L;
        String botCodeA = "";
        String botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }
        this.playerA = new Player(userIdA, botIdA, botCodeA, this.rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(userIdB, botIdB, botCodeB, 1, this.cols - 2, new ArrayList<>());
    }

    private boolean draw() {
        for (int i = 0;i < this.rows; i ++) {
            for (int j = 0; j < this.cols; j ++) {
                g[i][j] = 0;
            }
        }
        for (int i = 0; i < this.rows; i ++) {
            g[i][0] = g[i][this.cols - 1] = 1;
        }
        for (int i = 0; i < this.cols; i ++) {
            g[0][i] = g[rows - 1][i] = 1;
        }
        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount / 2; i ++) {
            for (int j = 0; j < 1000; j ++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 -c] == 1) {
                    continue;
                }
                if (r == 1 && c == this.cols - 2 || r == this.rows - 2 && c == 1) {
                    continue;
                }
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 -c] = 1;
                break;
            }
        }
        if(checkConnectivity(this.g, 1, this.cols - 2, this.rows - 2, 1)) {
            return true;
        }
        return false;
    }

    private boolean checkConnectivity(int[][] g, int sx, int sy, int ex, int ey) {
        if (sx == ex && sy == ey) {
            return true;
        }
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i ++) {
            int x = sx + dx[i];
            int y = sy + dy[i];
            if (g[x][y] == 1 || x < 0 || x >= this.rows || y < 0 || y >= this.cols) {
                continue;
            }
            if (checkConnectivity(g, x, y, ex, ey)) {
                g[sx][sy] = 0;
                return true;
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++) {
            if (draw()) {
                break;
            }
        }
    }

    private String getInput(Player player) {
        Player me, you;
        if (player.getUserId().equals(playerA.getUserId())) {
            me = playerA;
            you = playerB;
        }else {
            me = playerB;
            you = playerA;
        }
        return getMapString() +
                "#" + me.getSx() + "#" + me.getSy() + "#(" + me.getStepListString() + ")#" +
                you.getSx() + "#" + you.getSy() + "#(" + you.getStepListString() + ")";
    }

    private void sendBotCode(Player player) {
        System.out.println(player);
        if (player.getBotId() == -1) {
            return;
        }
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getUserId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));
        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    private boolean nextStep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendBotCode(playerA);
        sendBotCode(playerB);
        for (int i = 0; i < 50; i ++){
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (this.nextStepA != null && this.nextStepB != null) {
                        playerA.getStepList().add(nextStepA);
                        playerB.getStepList().add(nextStepB);
                        return true;
                    }
                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkNextStepValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if (g[cell.getX()][cell.getY()] == 1) {
            return false;
        }
        for(int i = 0; i < n - 1; i ++) {
            if (cell.getX() == cellsA.get(i).getX() && cell.getY() == cellsA.get(i).getY()
                || cell.getX() == cellsB.get(i).getX() && cell.getY() == cellsB.get(i).getY()) {
                return false;
            }
        }
        return true;
    }

    private void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        boolean validA = checkNextStepValid(cellsA, cellsB);
        boolean validB = checkNextStepValid(cellsB, cellsA);
        if (!validA || !validB) {
            status = "finish";
            if (!validA && !validB) {
                loser = "all";
            }else if (!validA) {
                loser = "A";
            }else {
                loser = "B";
            }
        }

    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.userSocketMap.get(playerA.getUserId()) != null) {
            WebSocketServer.userSocketMap.get(playerA.getUserId()).sendMessage(message);
        }
        if (WebSocketServer.userSocketMap.get(playerB.getUserId()) != null) {
            WebSocketServer.userSocketMap.get(playerB.getUserId()).sendMessage(message);
        }
    }
    private void sendMove() {
        JSONObject resp = new JSONObject();
        resp.put("event", "move");
        lock.lock();
        try {
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = nextStepB = null;
        }finally {
            lock.unlock();
        }
        sendAllMessage(resp.toJSONString());
    }

    public String getMapString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < rows; i ++){
            for(int j = 0;j < cols; j ++){
                stringBuilder.append(g[i][j]);
            }
        }
        return stringBuilder.toString();
    }

    private void sendToDatabase() {
        User userA = WebSocketServer.userMapper.selectById(playerA.getUserId());
        User userB = WebSocketServer.userMapper.selectById(playerB.getUserId());
        if ("A".equals(loser)) {
            userA.setRating(userA.getRating() - 5);
            userB.setRating(userB.getRating() + 10);
        }else {
            userA.setRating(userA.getRating() + 10);
            userB.setRating(userB.getRating() - 5);
        }
        WebSocketServer.userMapper.updateById(userA);
        WebSocketServer.userMapper.updateById(userB);
        Record record = Record.builder()
                .aId(playerA.getUserId())
                .aSx(playerA.getSx())
                .aSy(playerA.getSy())
                .bId(playerB.getUserId())
                .bSx(playerB.getSx())
                .bSy(playerB.getSy())
                .aSteps(playerA.getStepListString())
                .bSteps(playerB.getStepListString())
                .map(getMapString())
                .loser(loser)
                .build();
        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        sendToDatabase();
        sendAllMessage(resp.toJSONString());
    }
    @Override
    public void run() {
        for(int i = 0; i < 1000; i ++) {
            if (nextStep()) {
                judge();
                if (status.equals("playing")) {
                    sendMove();
                }else {
                    sendResult();
                    break;
                }
            }else {
                status = "finish";
                lock.lock();
                try {
                    if (this.nextStepA == null && this.nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                }finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
