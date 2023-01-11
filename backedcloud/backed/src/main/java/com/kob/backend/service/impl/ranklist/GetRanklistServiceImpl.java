package com.kob.backend.service.impl.ranklist;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.ranklist.GetRanklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetRanklistServiceImpl implements GetRanklistService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public IPage<User> getRanklist(Integer page, Integer count) {
        IPage<User> iPage = new Page<>(page, count);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(User::getRating);
        return userMapper.selectPage(iPage, queryWrapper);
    }
}
