package com.huajie.cache.serialize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.huajie.cache.RereCacheItem;

/**
 *
 * @author huajie
 */
public class CacheSerialBasic implements ICacheSerial {

    private String cachePath = "c:\\rere_cache";
    private String instanceName = "default";

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getCachePath() {
        return cachePath;
    }

    /**
     * 根据缓存名称构建缓存持久化器
     *
     * @param name
     */
    public CacheSerialBasic(String name) {
        this.instanceName = name;
        this.initCachePath();
    }

    /**
     * 初始化保存缓存对象的目录
     */
    private void initCachePath() {
        String pp = this.getClass().getClassLoader().getResource("").getPath();
        //System.out.println(pp);
        pp+="rere_cache";
        File file = new File(pp);
        if (!file.exists()) {
            file.mkdir();
        }
        
        this.cachePath = pp;
        String ppitems=this.fetchItemPath();
        //System.out.println(ppitems);
        
    }

    /**
     * 每次读，都要更新相应文件的最后修改时间，以在检查过期对象时能取得近期访问时间信息
     *
     * @param id
     * @return
     */
    @Override
    public RereCacheItem findItem(String id) {
        RereCacheItem ci = null;
        try {
            final File file = new File(fetchItemPath(id));
            if (file.exists()) {
                InputStream is = new FileInputStream(file);
                ObjectInputStream oon = new ObjectInputStream(is);
                ci = (RereCacheItem) oon.readObject();
                ci.setVisitTime(new Date());
                file.setLastModified(new Date().getTime());//重设缓存对象文件的最后修改时间
                oon.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ci;
    }

    @Override
    public void writeItem(RereCacheItem item) {
        this.initCachePath();//初始化缓存目录
        //保存缓存对象
        try {
            File file = new File(fetchItemPath(item.getId()));
            if (file.exists()) {
                OutputStream os = new FileOutputStream(file);
                ObjectOutputStream oon = new ObjectOutputStream(os);
                oon.writeObject(item);
                oon.close();
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addDefaultIndex(RereCacheItem item) {
        File file = new File(fetchIndexPath("default"));
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            PrintWriter pw = new PrintWriter(writer);
            pw.print(item.getGenTime().getTime());
            pw.print(",");
            pw.print(item.getId());
            pw.print("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void putIndexData(String indexName, String value, String id) {

        File file = new File(fetchIndexPath(indexName));
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            PrintWriter pw = new PrintWriter(writer);
            pw.print(value);
            pw.print(",");
            pw.print("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItem(String id) {
        try {
            File file = new File(fetchItemPath(id));
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkLives(long liveLen) {
        if (liveLen == 0) {
            return;
        }
        long now = System.currentTimeMillis();
        File dir = new File(this.fetchItemPath());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                long lastVisit = f.lastModified();
                long len = now - lastVisit;//已经闲置时间
                if (len >= liveLen) {
                    f.delete();
                }
            }
        }
    }

    /**
     * 将本实例中的所有持久化实例返回
     * @return 
     */
    public List<RereCacheItem> findAllItems() {
        List<RereCacheItem> list = new ArrayList();
        File dir = new File(this.fetchItemPath());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                RereCacheItem ci = this.findItem(f.getName());
                if (ci != null) {
                    list.add(ci);
                }
            }
        }
        return list;
    }

    private String fetchItemPath(String id) {
        return fetchItemPath() + "/" + id;
    }

    private String fetchItemPath() {
        String instPath=cachePath+ File.separator  + this.instanceName;
        File file = new File(instPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String itemPath=cachePath+ File.separator  + this.instanceName + "/" + "items";
        file = new File(itemPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return itemPath;
    }

    private String fetchIndexPath(String name) {
        return cachePath + File.separator + this.instanceName + File.separator + "indexes" + File.separator + name;
    }

}
