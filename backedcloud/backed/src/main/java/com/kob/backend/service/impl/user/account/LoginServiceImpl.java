package com.kob.backend.service.impl.user.account;

import com.kob.backend.bo.user.UserDetailsImpl;
import com.kob.backend.entity.User;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public String getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl)authenticate.getPrincipal();
        User user = userDetails.getUser();
        return JwtUtil.createJWT(user.getId().toString());
    }
}
