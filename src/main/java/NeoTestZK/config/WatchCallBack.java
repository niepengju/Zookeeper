package NeoTestZK.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class WatchCallBack implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    ZooKeeper zk;
    MyConf conf;

    CountDownLatch cc = new CountDownLatch(1);

    public MyConf getConf() {
        return conf;
    }

    public void setConf(MyConf conf) {
        this.conf = conf;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public void await(){
        zk.exists("/AppConf",this,this,"aaa");
        try {
            cc.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Watcher
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/AppConf",this,this,"aaa");
                System.out.println("Watcher NodeCreated");
                break;
            case NodeDeleted:
                conf.setConf("");
                cc = new CountDownLatch(1);
                System.out.println("Watcher NodeDeleted");
                break;
            case NodeDataChanged:
                zk.getData("/AppConf",this,this,"aaa");
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
        }
    }

    //DataCallBack
    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if(stat != null)
        {
            String data = new String(bytes);
            System.out.println("DataCallBack:"+ data);
            conf.setConf(data);
            cc.countDown();
        }

    }

    //StatCallBack
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if(stat !=null)
        {
            zk.getData("/AppConf",this,this,"aaa");
        }
    }
}
