package site.higgs.limiter.ratelimiter;

import site.higgs.limiter.Limiter;

import java.util.Map;

public abstract class RateLimiter implements Limiter<HRateLimiter> {


    public abstract boolean acquire(Object key, double rate, long capacity);


    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        double pps = (double) args.get("rate");
        long capacity = (long) args.get("capacity");
        return acquire(key, pps, capacity);
    }

    @Override
    public void release(Object key, Map<String, Object> args) {
        // do noting
    }
}
