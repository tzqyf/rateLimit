package com.ratelimit.demo.service;

public interface LimitService {
    String limitTest(String key,int limitCount,int time);
}
