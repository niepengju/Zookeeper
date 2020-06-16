package NeoTestZK.lock;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestLock {
    ZooKeeper zk;

    @Before
    public void getConn() throws InterruptedException {
        zk = ZKUtils.getZk();
    }

    @After
    public void close(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lock(){
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                WatchCallBack watchCallBack = new WatchCallBack();
                watchCallBack.setZk(zk);
                watchCallBack.setThreadName(Thread.currentThread().getName());
                try {
                    watchCallBack.tryLock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " do something");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                watchCallBack.unLock();
            }).start();
        }

        while(true){

        }
    }

}
