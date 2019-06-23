package site.higgs.limiter.peak;

import site.higgs.limiter.Limiter;

import java.util.Map;

public abstract class PeakLimiter implements Limiter<HPeak> {

    public abstract boolean acquire(Object key, int max);

    public abstract void release(Object key, int max);


    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        return acquire(key, (int) args.get("max"));
    }

    @Override
    public void release(Object key, Map<String, Object> args) {
        release(key, (int) args.get("max"));
    }
}
