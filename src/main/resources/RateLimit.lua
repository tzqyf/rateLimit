--lua 脚本
local key = "rate.limit:" .. KEYS[1] --限流KEY
local limit = tonumber(ARGV[1])        --限流大小
local time = tonumber(ARGV[2])
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then --如果超出限流大小 返回0
   return 0
else  --请求数一次key对应value值+1，并设置过期时间time
   redis.call("INCRBY", key,"1")
   redis.call("expire", key,time)
   return current + 1
end