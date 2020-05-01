package com.ratelimit.demo.config;

import com.ratelimit.demo.aop.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Configuration
public class LimitAspect {
    private static final Logger logger = LoggerFactory.getLogger(LimitAspect.class);

    @Autowired
    private RedisTemplate<String,Serializable> limitRedisTemplate;

    @Autowired
    private DefaultRedisScript<Number> redisluaScript;

    @Pointcut("@annotation(com.ratelimit.demo.aop.RateLimit)")
    private void pointCut(){

    }

    /**
     * aop环绕形 进行限制流处理
     *
     * 我这里基于业务为了做成可配置化限流 是从切面代理方法中获取获取限流参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("jingruqiemian");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        if (rateLimit != null) {
            Object[] args = joinPoint.getArgs();
            //从代理方法中获取限流参数
            String key = (String) args[0];
            int limitCount = (int) args[1];
            int time = (int) args[2];
            List<String> keys = new ArrayList<>();
            keys.add(key);
            Number number = limitRedisTemplate.execute(redisluaScript, keys,limitCount, time);
            if (number != null && number.intValue() != 0 && number.intValue() <= limitCount) {
                logger.info("限流时间段内访问第：{} 次", number.toString());
                return joinPoint.proceed();
            }
            System.out.println("剩余令牌------------： "+number);
        } else {
            return joinPoint.proceed();
        }
        throw new RuntimeException("已经到设置限流次数");
    }

}
