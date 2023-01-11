package com.kob.backend.service.ranklist;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.entity.User;

public interface GetRanklistService {
    IPage<User> getRanklist(Integer page, Integer count);
}
