package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.exception.http.ParameterException;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.entity.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void register(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null){
            throw new ParameterException(1002);
        }
        String photo = "https://cdn.acwing.com/media/user/profile/photo/15396_lg_99a90c6efe.jpg";
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .photo(photo)
                .build();
        this.userMapper.insert(user);
    }
}
