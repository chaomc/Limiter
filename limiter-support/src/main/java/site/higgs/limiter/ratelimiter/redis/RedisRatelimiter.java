package site.higgs.limiter.ratelimiter.redis;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.higgs.limiter.ratelimiter.RateLimiter;

public class RedisRatelimiter extends RateLimiter {

    private static final Logger logger = LoggerFactory.getLogger(RedisRatelimiter.class);

    private String limiterName;

    private RateLimiterRedission ratelimiterRedission;

    /**
     * @param limiterName
     * @param config
     */
    public RedisRatelimiter(String limiterName, Config config) {
        this.limiterName = limiterName;
        this.ratelimiterRedission = new RateLimiterRedission(config);
        logger.info("RedisRateLimiter named {} start success!", limiterName);

    }

    @Override
    public boolean acquire(Object key, double rate, long capacity) {
        RedisRatelimiterObject rateLimiterObject = ratelimiterRedission.getRedisRatelimiterObject(key.toString());
        return rateLimiterObject.tryAcquire(1, rate, capacity);
    }

    @Override
    public String getLimiterName() {
        return limiterName;
    }


    /**
     * 继承自Redisson 实现自定义api
     */
    public static class RateLimiterRedission extends Redisson {


        public RateLimiterRedission(Config config) {
            super(config);
        }

        public RedisRatelimiterObject getRedisRatelimiterObject(String name) {
            return new RedisRatelimiterObject(connectionManager.getCommandExecutor(), name);
        }
    }

}
