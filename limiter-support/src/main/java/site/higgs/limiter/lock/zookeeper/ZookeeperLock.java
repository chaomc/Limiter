package site.higgs.limiter.lock.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.higgs.limiter.lock.Lock;

public class ZookeeperLock extends Lock {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);

    private String lockName;

    private String basePath;

    private CuratorFramework client;


    public ZookeeperLock(String lockName, String basePath, CuratorFramework client) {
        this.lockName = lockName;
        this.basePath = basePath;
        this.client = client;
        if (!client.getState().equals(CuratorFrameworkState.STARTED)) {
            client.start();
        }

        logger.info("zookeeper lock named {} start success!", lockName);
    }

    public ZookeeperLock(String lockName, CuratorFramework client) {
        this(lockName, "/locks/", client);
    }

    @Override
    public boolean lock(Object key) {
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(basePath + key.toString());
            if (logger.isDebugEnabled()) {
                logger.debug("lock success on {}", key);
            }
            return true;
        } catch (KeeperException.NodeExistsException e) {
            logger.info("lock fail on {}", key);
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void unlock(Object key) {

        try {
            client.delete().forPath(basePath + key.toString());
        } catch (KeeperException.NodeExistsException e) {
            throw new IllegalMonitorStateException("You do not own the lock: " + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getLimiterName() {
        return lockName;
    }
}
