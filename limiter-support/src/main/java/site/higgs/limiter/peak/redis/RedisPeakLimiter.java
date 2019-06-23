package site.higgs.limiter.peak.redis;

import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import site.higgs.limiter.peak.PeakLimiter;

public class RedisPeakLimiter extends PeakLimiter {
    private Redisson redisson;

    private String limiterName;

    public RedisPeakLimiter(Redisson redisson, String limiterName) {
        this.redisson = redisson;
        this.limiterName = limiterName;
        try {

        }finally {

        }
    }

    @Override
    public boolean acquire(Object key, int max) {
        RSemaphore rSemaphore = redisson.getSemaphore(key.toString());
        return rSemaphore.tryAcquire();
    }

    @Override
    public void release(Object key, int max) {
        RSemaphore rSemaphore = redisson.getSemaphore(key.toString());
        rSemaphore.release();
    }

    @Override
    public String getLimiterName() {
        return limiterName;
    }
}
