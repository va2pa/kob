package com.kob.matchingsystem;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import com.kob.matchingsystem.service.impl.util.MatchPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchingsystemApplication.class, args);
        MatchingServiceImpl.matchPoolService.start();
    }

}
