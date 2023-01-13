package com.kob.backend.controller.ranklist;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kob.backend.entity.User;
import com.kob.backend.service.ranklist.GetRanklistService;
import com.kob.backend.vo.PageResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranklist/")
public class GetRanklistController {
    @Autowired
    private GetRanklistService getRanklistService;

    @GetMapping("getlist/")
    private PageResponseVO<User> getRanklist(@RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer count) {
        IPage<User> ranklistPage = getRanklistService.getRanklist(page, count);
        PageResponseVO<User> pageResponseVO = new PageResponseVO<>();
        pageResponseVO.setPage(page);
        pageResponseVO.setCount(count);
        pageResponseVO.setTotal(ranklistPage.getTotal());
        pageResponseVO.setItems(ranklistPage.getRecords());
        return pageResponseVO;
    }
}
