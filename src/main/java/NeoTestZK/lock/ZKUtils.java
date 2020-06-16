package NeoTestZK.lock;

import NeoTestZK.config.DefaultWatch;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtils {
    private static ZooKeeper zk;

    private static String address = "127.0.0.1:2181/testLock";

    private static DefaultWatch watch = new DefaultWatch();
    static CountDownLatch init = new CountDownLatch(1);

    public static ZooKeeper getZk() throws InterruptedException {
        try {
            zk = new ZooKeeper(address,1000,watch);
            watch.setCc(init);
        } catch (IOException e) {
            e.printStackTrace();
        }
        init.await();
        return zk;
    }



}
