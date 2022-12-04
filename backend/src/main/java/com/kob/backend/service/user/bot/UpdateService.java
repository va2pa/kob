package com.kob.backend.service.user.bot;

public interface UpdateService {
    void update(Long botId, String title, String description, String content);
}
