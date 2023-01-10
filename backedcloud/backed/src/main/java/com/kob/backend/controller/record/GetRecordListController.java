package com.kob.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.entity.Record;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.record.GetRecordListService;
import com.kob.backend.vo.PageResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/record/")
public class GetRecordListController {

    @Autowired
    private GetRecordListService getRecordListService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("getlist/")
    public PageResponseVO<JSONObject> getRecordList(@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer count) {
        IPage<Record> recordPage = getRecordListService.getRecordList(page, count);
        List<JSONObject> items = new ArrayList<>();
        for (Record record : recordPage.getRecords()) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            String result = "平局";
            if ("A".equals(record.getLoser())) {
                result = "B胜";
            }else if("B".equals(record.getLoser())) {
                result = "A胜";
            }
            item.put("result", result);
            item.put("record", record);
            items.add(item);
        }
        PageResponseVO<JSONObject> resp = new PageResponseVO<>();
        resp.setItems(items);
        resp.setPage(page);
        resp.setCount(count);
        resp.setTotal(recordPage.getTotal());

        return resp;
    }
}
