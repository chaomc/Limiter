package site.higgs.limiter.lock;

import site.higgs.limiter.Limiter;

import java.util.Map;

/**
 * Lock
 */
public abstract class Lock implements Limiter<HLock> {


    public abstract boolean lock(Object key);

    public abstract void unlock(Object key);


    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        return lock(key);
    }

    @Override
    public void release(Object key, Map<String, Object> args) {
        unlock(key);
    }
}
