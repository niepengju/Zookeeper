package NeoTestZK.config;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class TestConfig {
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
    public void getConf(){

        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setZk(zk);
        MyConf myConf = new MyConf();
        watchCallBack.setConf(myConf);

        zk.exists("/AppConf",watchCallBack,watchCallBack,"acc");
        System.out.println("TestConfig Pending...");
        watchCallBack.await();
        System.out.println("TestConfig doing...");

        while(true){
            System.out.println("TestConfig While True...");
            if(myConf.getConf().equals(""))
            {
                System.out.println("data is missing ...");
                watchCallBack.await();
            }
            else
            {
                System.out.println(myConf.getConf());
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
