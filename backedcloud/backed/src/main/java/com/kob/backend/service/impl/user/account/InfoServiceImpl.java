package com.kob.backend.service.impl.user.account;

import com.kob.backend.bo.user.UserDetailsImpl;
import com.kob.backend.entity.User;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class InfoServiceImpl implements InfoService {
    @Override
    public User getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        User loginUser = userDetails.getUser();
        return loginUser;
    }
}
