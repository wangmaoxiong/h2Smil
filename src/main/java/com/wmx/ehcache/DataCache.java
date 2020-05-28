package com.wmx.ehcache;

import org.ehcache.UserManagedCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.ehcache.config.builders.UserManagedCacheBuilder.newUserManagedCacheBuilder;

/**
 * @author wangmaoxiong
 * @version 1.0
 * @date 2020/5/22 17:43
 */
public class DataCache {
    private UserManagedCache<String, List<String>> cache;
    private static final Logger logger = LoggerFactory.getLogger(DataCache.class);

    public void setupCache() {
        cache = (UserManagedCache<String, List<String>>) (UserManagedCache<?, ?>)
                newUserManagedCacheBuilder(String.class, List.class)
                        .identifier("data-cache")
                        .build(true);
        logger.info("Cache setup is done");
    }

    public List<String> getFromCache() {
        return cache.get("all-peeps");
    }

    public void addToCache(List<String> line) {
        cache.put("all-peeps", line);
    }

    public void clearCache() {
        cache.clear();
    }

    public void close() {
        cache.close();
    }

    public static void main(String[] args) {
        DataCache dataCache = new DataCache();
        dataCache.setupCache();

        List<String> dataList = new ArrayList<>();
        dataList.add("秦国");
        dataList.add("韩国");
        dataList.add("楚国");
        dataCache.addToCache(dataList);

        List<String> fromCache = dataCache.getFromCache();
        System.out.println(fromCache);
    }
}
