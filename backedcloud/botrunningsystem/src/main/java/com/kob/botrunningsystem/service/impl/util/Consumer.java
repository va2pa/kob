package com.kob.botrunningsystem.service.impl.util;

import com.kob.botrunningsystem.util.BotCodeInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class Consumer extends Thread{

    private Bot bot;

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }
    private static final String receiveBotMoveUrl = "http://127.0.0.1:5000/pk/receive/bot/move/";

    public void startTimeout(int timeout, Bot bot) {
        this.bot = bot;
        this.start();
        try {
            this.join(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            this.interrupt();
        }
    }

    private String addUUID(String code, String uuid) {
        int index = code.indexOf(" implements com.kob.botrunningsystem.util.BotCodeInterface");
        return code.substring(0, index) + uuid + code.substring(index);
    }

    @Override
    public void run() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        System.out.println(addUUID(bot.getBotCode(), uuid));
        Supplier<Integer> botCodeInterface = Reflect.compile(
                "com.kob.botrunningsystem.util.BotCode" + uuid,
                addUUID(bot.getBotCode(), uuid)
        ).create().get();
        File file = new File("input.txt");
        try(PrintWriter fout = new PrintWriter(file)) {
            fout.println(bot.getInput());
            fout.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Integer direction = botCodeInterface.get();
        sendResult(direction);
    }

    private void sendResult(Integer direction) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());
        Consumer.restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }

}
