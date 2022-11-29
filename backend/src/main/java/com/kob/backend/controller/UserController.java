package com.kob.backend.controller;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("add/{username}/{password}/")
    public String add(@PathVariable String username,
                      @PathVariable String password){
        User user = new User();
        password = passwordEncoder.encode(password);
        user.setUsername(username);
        user.setPassword(password);
        userMapper.insert(user);
        return "successfully";
    }

    @GetMapping("all/")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }
}
