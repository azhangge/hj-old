package com.huajie.cache.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.huajie.cache.CacheOperations;
import com.huajie.cache.CacheRequestMessage;
import com.huajie.cache.CacheResponseMessage;
import com.huajie.cache.RereCacheItem;
import com.huajie.cache.RereCacheUtil;
import com.huajie.cache.remote.RemoteCacheItem;

/**
 *
 * @author huajie
 */
public class RereCacheClient implements Serializable {
    
    private String serverAddr = "localhost";
    private int serverPort = 62602;
    
    private Socket socket = null;
    
    public String getServerAddr() {
        return serverAddr;
    }
    
    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
    
    public int getServerPort() {
        return serverPort;
    }
    
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public void put(String instanceName, String key, Object value) {
        try {
            socket = new Socket(serverAddr, serverPort);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            
            CacheRequestMessage msg = new CacheRequestMessage();
            msg.setInstanceName(instanceName);
            RemoteCacheItem rc=new RemoteCacheItem();
            rc.setId(key);
            rc.setJson(RereCacheUtil.toJson(value));
            msg.setItem(rc);
            msg.setOperation(CacheOperations.UPDATE);
            
            oos.writeObject(msg);
            oos.flush();
            oos.close();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public RereCacheItem find(String instanceName, String key, Class cal) {
        RereCacheItem obj = null;
        try {
            socket = new Socket(serverAddr, serverPort);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            ObjectInputStream ois = new ObjectInputStream(is);
            
            CacheRequestMessage msg = new CacheRequestMessage();
            msg.setInstanceName(instanceName);
            msg.setOperation(CacheOperations.FIND_ALL);
            oos.writeObject(msg);
            oos.flush();
            CacheResponseMessage res = (CacheResponseMessage) ois.readObject();
            obj = RereCacheUtil.fromRemote(res.getItem(), cal);
            oos.close();
            os.close();
            ois.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
    
    public List<RereCacheItem> findAll(String instanceName, Class cal) {
        List<RereCacheItem> objs = new ArrayList();
        try {
            socket = new Socket(serverAddr, serverPort);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            ObjectInputStream ois = new ObjectInputStream(is);
            
            CacheRequestMessage msg = new CacheRequestMessage();
            msg.setInstanceName(instanceName);
            msg.setOperation(CacheOperations.FIND_ALL);
            oos.writeObject(msg);
            oos.flush();
            CacheResponseMessage res = (CacheResponseMessage) ois.readObject();
            List<RemoteCacheItem> objs2 = res.getItems();
            for (RemoteCacheItem it : objs2) {
                objs.add(RereCacheUtil.fromRemote(it, cal));
            }
            oos.close();
            os.close();
            ois.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objs;
    }
    
    public List<RereCacheItem> queryEqual(String instanceName, String indexName,String keyWord,Class cal) {
        List<RereCacheItem> objs = new ArrayList();
        try {
            socket = new Socket(serverAddr, serverPort);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            ObjectInputStream ois = new ObjectInputStream(is);
            
            CacheRequestMessage msg = new CacheRequestMessage();
            msg.setInstanceName(instanceName);
            msg.setOperation(CacheOperations.QUERY_EQUAL);
            msg.setQueryIndex(indexName);
            msg.setKeyWord(keyWord);
            oos.writeObject(msg);
            oos.flush();
            
            CacheResponseMessage res = (CacheResponseMessage) ois.readObject();
            List<RemoteCacheItem> objs2 = res.getItems();
            objs=RereCacheUtil.fromRemoteList(objs2, cal);
            
            oos.close();
            os.close();
            ois.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return objs;
    }
    
    public void delete(String instanceName, String key) {
        try {
            socket = new Socket(serverAddr, serverPort);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            
            CacheRequestMessage msg = new CacheRequestMessage();
            msg.setInstanceName(instanceName);
            
            RemoteCacheItem rc=new RemoteCacheItem();
            rc.setId(key);
            msg.setItem(rc);
            
            msg.setOperation(CacheOperations.DELETE);
            oos.writeObject(msg);
            oos.flush();
            oos.close();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 批量删除itmes中的数据，item设置了id即可
     * @param instanceName
     * @param items 
     */
    public void batchDelete(String instanceName, List<RereCacheItem> items) {
        try {
            socket = new Socket(serverAddr, serverPort);
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            
            CacheRequestMessage msg = new CacheRequestMessage();
            msg.setInstanceName(instanceName);
            
            msg.setItems(RereCacheUtil.toRemoteList(items));
            
            msg.setOperation(CacheOperations.BATCH_DELETE);
            oos.writeObject(msg);
            oos.flush();
            oos.close();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String args[]) {
        RereCacheClient client = new RereCacheClient();
        String name = "test";
//        String key = "age";
//        Integer value = 6;
//        client.put(name, key, value);
        List<RereCacheItem> items = client.findAll(name,Integer.class);
        System.out.println(items);
        for (RereCacheItem rc : items) {
            System.out.println(rc.getObject());
        }
    }
}
