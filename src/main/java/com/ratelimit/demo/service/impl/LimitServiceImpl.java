package com.ratelimit.demo.service.impl;

import com.ratelimit.demo.aop.RateLimit;
import com.ratelimit.demo.service.LimitService;
import org.springframework.stereotype.Service;

@Service("limitService")
public class LimitServiceImpl implements LimitService {
    @RateLimit
    @Override
    public String limitTest(String key, int limitCount, int time) {
        return "可以调用";
    }
}
