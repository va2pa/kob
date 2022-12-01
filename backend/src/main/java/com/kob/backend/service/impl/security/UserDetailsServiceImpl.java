package com.kob.backend.service.impl.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.bo.user.UserDetailsImpl;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            throw new RuntimeException("用户不存在");
        }
        return new UserDetailsImpl(user);
    }
}
