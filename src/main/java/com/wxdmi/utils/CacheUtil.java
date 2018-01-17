package com.wxdmi.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by ryan on 15/5/13.
 * 缓存工具
 * 一级缓存redis
 * 先从二级缓存读，如果没有从一级缓存读，并放入二级缓存中
 */
public class CacheUtil {

    public static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    /**
     * 最大缓存数量
     */
    private Long maxCacheNum;
    /**
     * 写完之后过期时间
     */
    private Long expireAfterWriteTimes;

    public long getMaxCacheNum() {
        return maxCacheNum;
    }

    public void setMaxCacheNum(long maxCacheNum) {
        this.maxCacheNum = maxCacheNum;
    }

    public long getExpireAfterWriteTimes() {
        return expireAfterWriteTimes;
    }

    public void setExpireAfterWriteTimes(long expireAfterWriteTimes) {
        this.expireAfterWriteTimes = expireAfterWriteTimes;
    }

//    LoadingCache<String, Object> cache;
    Cache<String, Object> cache;

    public void initCache(){
        cache = CacheBuilder.newBuilder()
                .maximumSize(maxCacheNum)
                .concurrencyLevel(4)
                .expireAfterWrite(expireAfterWriteTimes, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 从一级缓存中读取并放入二级缓存中
     * @param key
     * @return
     */
    public <T> T get(String key, Class<T> cls){
        Object obj = getCacheByFromRedis(key);
        if(obj != null)
            return (T) obj;
        return null;
    }
    public void put(String key, Object value){
        cache.put(key, value);
    }

    public Object getCacheByFromRedis(String key){
        Object result = null;
        try{
            result = cache.get(key, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return null;
                }
            });
            return result;
        } catch (Exception e){
//            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * 从其他地方读取放入二级缓存中
     * @param key
     * @param call
     * @return
     */
    public <T> T getCacheByFromOther(String key, Callable<Object> call, Class<T> cls){
        Object obj = getCacheByFromOther(key, call);
        if(obj != null)
            return (T) obj;
        return null;
    }
    public Object getCacheByFromOther(String key, Callable<Object> call){
        try {
            return cache.get(key, call);
        } catch (ExecutionException e) {
//            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 三级读取，先从本地cache读取，再从redis读取，最后从自己定义的callable中读取
     * @param key
     * @param call
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getCacheRedisFromOther(String key, Callable<Object> call, Class<T> cls){
        Object obj = getCacheRedisFromOther(key, call);
        if(obj != null)
            return (T) obj;
        return null;
    }
    public Object getCacheRedisFromOther(String key, Callable<Object> call){
        try{
            Object obj = getCacheByFromRedis(key);
            if(null == obj)
                obj = call.call();
            return obj;
        } catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    public Object getCacheForeverByFromRedis(String key){
        return getCacheByFromRedis(key);
    }

    public void invalid(String key){
        cache.invalidate(key);
    }

    public void invalidAll(){
        cache.invalidateAll();
    }

    public ConcurrentMap<String, Object> getAllCache(){
        return cache.asMap();
    }


}
