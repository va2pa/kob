package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.entity.Record;

public interface GetRecordListService {
    IPage<Record> getRecordList(Integer page, Integer count);
}
