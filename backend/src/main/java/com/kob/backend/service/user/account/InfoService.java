package com.kob.backend.service.user.account;

import com.kob.backend.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface InfoService {
    User getInfo();
}
