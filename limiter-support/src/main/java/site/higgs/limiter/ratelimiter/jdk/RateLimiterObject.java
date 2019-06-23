package site.higgs.limiter.ratelimiter.jdk;

/**
 * 基于令牌桶实现的速率限制器
 */
public class RateLimiterObject {

    private double rate;

    private long capacity;

    private long lastSyncTime;

    private double storedPermits;


    public synchronized boolean tryAcquire(long permits, double rate, long capacity) {
        if (permits > capacity) return false;
        long now = System.currentTimeMillis();
        if (rate != this.rate || capacity != this.capacity) {
            this.rate = rate;
            this.capacity = capacity;
            this.storedPermits = capacity - permits;
            this.lastSyncTime = now;
            return true;
        }
        resync(now);
        if (storedPermits >= permits) {
            storedPermits = storedPermits - permits;
            return true;
        }
        return false;
    }


    private void resync(long nowMicros) {
        double newPermits = (nowMicros - lastSyncTime) * rate / 1000 + storedPermits;
        storedPermits = Math.max(newPermits, capacity);
        this.lastSyncTime = nowMicros;
    }
}
