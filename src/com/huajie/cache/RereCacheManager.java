package com.huajie.cache;

/**
 * 缓存管理器
 *
 * @author Administrator
 */
public class RereCacheManager {

    private static RereCacheMemoryPool container = null;

    /**
     * 检查缓存容器是否已经创建并已经启动
     */
    private static void checkContainer() {
        if (container == null) {
            container = new RereCacheMemoryPool();
            new Thread(container).start();
        }
    }

    /**
     * 返回缓存容器
     *
     * @return
     */
    public static RereCacheMemoryPool getContainer() {
        return container;
    }

    /**
     * 返回一个缓存实例，如果不存在，则创建一个
     *
     * @param name 缓存实例名
     * @return
     */
    public static IRereCacheInstance getReplicatedInstance(String name) {
        checkContainer();
        IRereCacheInstance ci = container.getInstancesMap().get(name);
        if (ci == null) {
            ci = new RereCacheInstanceReplicated(name);
            //ci.setLifeLen(600);
            container.getInstancesMap().put(name, ci);
        }
        return ci;
    }

    /**
     * 返回一个缓存实例，如果不存在，则创建一个
     *
     * @param name 缓存实例名
     * @return
     */
    public static IRereCacheInstance getLocalInstance(String name) {
        checkContainer();
        IRereCacheInstance ci = container.getInstancesMap().get(name);
        if (ci == null) {
            ci = new RereCacheInstanceLocal(name);
            //ci.setLifeLen(600);
            container.getInstancesMap().put(name, ci);
        }
        return ci;
    }
    
    /**
     * 返回一个缓存实例，如果不存在，则创建一个
     *
     * @param name 缓存实例名
     * @return
     */
    public static void removeInstance(String name) {
        checkContainer();
        IRereCacheInstance ci = container.getInstancesMap().remove(name);
        
    }

    /**
     * 停止缓存容器运行
     */
    public static void stop() {
        if (container != null) {
            container.ifOpen = false;
        }

    }


}
