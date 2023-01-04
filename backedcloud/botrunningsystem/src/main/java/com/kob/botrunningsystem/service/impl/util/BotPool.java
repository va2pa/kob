package com.kob.botrunningsystem.service.impl.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Queue<Bot> queue = new LinkedList<>();

    public void addBot(Long botId, String botCode, String input) {
        try {
            lock.lock();
            queue.add(new Bot(botId, botCode, input));
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    private void consume(Bot bot) {

    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (queue.isEmpty()) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else {
                Bot bot = queue.remove();
                lock.unlock();
                consume(bot);
            }
        }
    }
}
