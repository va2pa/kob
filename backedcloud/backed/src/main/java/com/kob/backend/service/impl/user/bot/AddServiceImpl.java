package com.kob.backend.service.impl.user.bot;

import com.kob.backend.bo.user.UserDetailsImpl;
import com.kob.backend.entity.Bot;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public void add(String title, String description, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        User user = userDetails.getUser();
        if (description.length() == 0){
            description = "这个用户很懒，什么都没留下~";
        }
        Bot bot = Bot.builder()
                .userId(user.getId())
                .title(title)
                .description(description)
                .content(content)
                .rating(1500L)
                .build();
        botMapper.insert(bot);
    }
}
