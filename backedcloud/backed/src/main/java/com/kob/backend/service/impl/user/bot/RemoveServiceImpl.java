package com.kob.backend.service.impl.user.bot;

import com.kob.backend.bo.user.UserDetailsImpl;
import com.kob.backend.entity.Bot;
import com.kob.backend.entity.User;
import com.kob.backend.exception.http.ForbiddenException;
import com.kob.backend.exception.http.ParameterException;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public void remove(Long botId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        User user = userDetails.getUser();
        Bot bot = botMapper.selectById(botId);
        if (bot == null){
            throw new ParameterException(2000);
        }
        if (!user.getId().equals(bot.getUserId())) {
            throw new ForbiddenException(2001);
        }
        botMapper.deleteById(botId);
    }
}
