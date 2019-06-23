package site.higgs.limiter.lock.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import site.higgs.limiter.lock.Lock;

public class RedisLock extends Lock {

    private RedissonClient redisson;

    private String lockName;

    public RedisLock(RedissonClient redisson, String lockName) {
        this.redisson = redisson;
        this.lockName = lockName;
    }

    @Override
    public boolean lock(Object key) {
        RLock rLock = redisson.getLock(key.toString());
        return rLock.tryLock();
    }

    @Override
    public void unlock(Object key) {
        RLock rLock = redisson.getLock(key.toString());
        rLock.unlock();
    }

    @Override
    public String getLimiterName() {
        return lockName;
    }
}
