package com.platform;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class PropertiesReload extends PropertyPlaceholderConfigurer {

    private Log log = LogFactory.getLog(PropertiesReload.class);

    private String env;

    private String appName;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory,
                                     Properties props) throws BeansException {
        env = System.getProperty("app.env");
        appName = System.getProperty("app.name");
        Properties prop = this.loadProp();
        super.processProperties(beanFactory, prop);
    }

    public Properties loadProp() {
        Properties prop = new Properties();
        try {
            Map<String,Object> map = new HashMap<>();
            map = this.loadZk();
            for(Entry<String, Object> entry:map.entrySet()) {
                prop.put(entry.getKey(), entry.getValue());
            }
            this.setLocations(new ClassPathResource("j2cache.properties"));
        } catch (Exception e) {
            log.error("failed to init properties", e);
        }
        return prop;
    }
    /**
     * 链接zookeeper，并且加载所有节点的信息，这里会用递归去读取所有节点的信息，
     * 为了方便读取，节点最多为两个节点，比如：
     * 			root1
     * 				node1
     * 				node2
     * 			root2
     * 				node3
     * 				node4
     * 每个节点都可以存储信息，这样定义完全足够一个项目使用了。
     * 节点存储信息格式： key:value;
     * 			  key1:value1;
     * 			  key2:value2;
     * 提示：为了多个项目公用同一个zookeeper，可以一个项目一个节点，指定的项目去加载指定的节点
     * 、 这里是刚入手zk，就写的自己能看的懂的。
     * @return
     */
    public Map<String,Object> loadZk(){
        Map<String,Object> map = new  HashMap<>();
        String connectString = "test.huashili.io:2181";
        int sessionTimeout = 50000;
        Watcher watcher = new Watcher() {
            //监控所有被触发的事件
            @Override
            public void process(WatchedEvent watchedevent) {
                System.out.println("已经触发了："+watchedevent.getType()+"事件");
            }
        };
        try {
            ZooKeeper zk = new ZooKeeper(connectString, sessionTimeout, watcher);
            loadZkInfo(map,zk);//加载节点信息
        } catch (Exception e) {
            log.error("load zookeeper fail exception", e);
        }
        return map;
    }


    /**
     * 加载zk 节点信息
     * @param path
     * @param map
     * @param zk
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void loadZkInfo(String path, Map<String,Object> map, ZooKeeper zk) throws KeeperException, InterruptedException {
        List<String> nodes = zk.getChildren(path, false);//加载主节点信息
        for(String node : nodes) {
            String subPath = "";
            if(path.equals("/")) {
                if(!node.equals(appName)) {
                    continue;
                }
                subPath = path + node;
            } else {
                subPath = path + "/" + node;
                if(!subPath.contains(env)) {
                    continue;
                }
            }
            loadZkInfo(subPath, map, zk);
            String pathData = new String(zk.getData(subPath, false, null));
            if(!StringUtils.isEmpty(pathData)) {
                String key = subPath.substring(subPath.lastIndexOf("/") + 1);
                map.put(key, pathData);
            }
        }
    }


    public void loadZkInfo(Map<String,Object> map, ZooKeeper zk) throws KeeperException, InterruptedException {
        loadZkInfo("/", map, zk);
    }
}
