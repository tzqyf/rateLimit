package com.ratelimit.demo.controller;

import com.ratelimit.demo.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/limit")
public class LimitController {
    @Autowired
    private LimitService limitService;

    @RequestMapping("test")
    public String test(String key){
        //业务场景下这些参数是存在缓存
        return limitService.limitTest("Limit_Chang_Test_"+key,10,20);
    }
}
