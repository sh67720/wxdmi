package com.wxdmi.utils.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wxdmi.constant.CacheConstant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by on 15/5/13.
 * 缓存工具
 * 一级缓存redis
 * 先从二级缓存读，如果没有从一级缓存读，并放入二级缓存中
 */
public class CacheUtil {

    public static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    /**
     * 最大缓存数量
     */
    @NotNull
    private Long maxCacheNum;
    /**
     * 写完之后过期时间
     */
    @NotNull
    private Long expireAfterWriteTimes;
    /**
     * 刷新后缓存多少时间
     */
    @NotNull
    private Long refreshExpireTimes;

    private Boolean bGetFromRedis = false;

    private RedisClusterClient redisClusterClient;

    public void setbGetFromRedis(Boolean bGetFromRedis) {
        this.bGetFromRedis = bGetFromRedis;
    }

    public void setRedisClusterClient(RedisClusterClient redisClusterClient) {
        this.redisClusterClient = redisClusterClient;
    }

    public long getMaxCacheNum() {
        return maxCacheNum;
    }

    public void setMaxCacheNum(long maxCacheNum) {
        this.maxCacheNum = maxCacheNum;
    }

    public long getRefreshExpireTimes() {
        return refreshExpireTimes;
    }

    public void setRefreshExpireTimes(long refreshExpireTimes) {
        this.refreshExpireTimes = refreshExpireTimes;
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
//                .refreshAfterWrite(refreshExpireTimes, TimeUnit.SECONDS)
                .build();
//                .build(new CacheLoader<String, Object>() {
//                    @Override
//                    public Object load(String key) throws Exception {
//                        Object result = null;
//                        if(bGetFromRedis){
//                            result = redisClusterClient.getObjectFromRedis(key);
//                        }
//                        return result;
//                    }
//                });

    }

    /**
     * 短暂存储
     * @param key
     * @param value
     */
    public void put2RedisCacheByShortTime(@NotNull String key, Object value){
        try{
            if(value != null)
                redisClusterClient.persitenceObjectByShortTime(key, value);
        } catch (Exception e){
            logger.error("persitenceObject to redis error, key:{}, value:{}", key, value);
        }
    }

    /**
     * 放入二级缓存中(普通性存储)
     * @param key
     * @param value
     */
    public void put2RedisCache(@NotNull String key, Object value){
        try{
            if(value != null)
                redisClusterClient.persitenceObject(key, value);
        } catch (Exception e){
            logger.error("persitenceObject to redis error, key:{}, value:{}", key, value);
        }
    }

    /**
     * 将session放入redis中
     * @param key
     * @param value
     */
    public void putSession2Redis(@NotNull String key, Object value){
        try{
            if(value != null)
                redisClusterClient.persitenceSessionObject(key, value);
        } catch (Exception e){
            logger.error("putSession2Redis to redis error, key:{}, value:{}", key, value);
        }
    }

    /**
     * 将全局性资源放入其中
     * @param key
     * @param value
     */
    public void putResouce2Redis(@NotNull String key, Object value){
        try{
            if(value != null)
                redisClusterClient.persitenceResource(key, value);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("putSession2Redis to redis error, key:{}, value:{}", key, value);
        }
    }

    /**
     * 将数据持久化到redis中
     * @param key
     * @param value
     */
    public void putData2RedisByForever(@NotNull String key, Object value){
        try{
            if(value != null)
                redisClusterClient.persitenceObjectByForever(key, value);
        } catch (Exception e){
            logger.error("putData2RedisByForever to redis error, key:{}, value:{}", key, value);
        }
    }

    /**
     * 将数据保存至redis中，单位：秒
     * @param key
     * @param second
     * @param value
     */
    public void putData2RedisByTime(@NotNull String key, int second, Object value){
        try{
            if(value != null)
                redisClusterClient.persitenceObject(key, second, value);
        } catch (Exception e){
            logger.error("putData2RedisByTime to redis error, key:{}, second:{}, value:{},cause:{}", key, second, value,e.getCause());
        }
    }

    public void putData2RedisByTime(@NotNull String key, int second, byte[] buffer){
        try{
            if(buffer != null)
                redisClusterClient.persitenceByte(key, second, buffer);
        } catch (Exception e){
            logger.error("putData2RedisByTime to redis error, key:{}, second:{}, value:{}", key, second, buffer);
        }
    }

    /**
     * 返回值为：true时可以操作，表明之前没有该值；false，则相反
     * @param key
     * @param second
     * @param value
     * @return
     */
    public boolean putData2RedisCache(@NotNull String key, int second, Object value){
        boolean flag = true;
        try{
            if(value != null) {
                flag = (1 == redisClusterClient.persitenceObjectWithoutTime(key, value)) ? true : false;
                if(flag)
                    redisClusterClient.expireKeyByByte(key, second);
            }
        } catch (Exception e){
            logger.error("putData2RedisCache to redis error, key:{}, value:{}", key, value);
        }
        return flag;
    }

    /**
     * 返回过期时间，
     * @param key
     * @return null / value
     */
    public Long getExpireTime(@NotNull String key){
        try{
            long expireTime = redisClusterClient.getExpireTime(key);
            if(expireTime <= 0) return null;

            return expireTime;
        } catch (Exception e){
            logger.error("getExpireTime from redis error, key:{}", key);
            return null;
        }
    }


    /**
     * 公用加锁方法
     * @param key
     * @param second
     * @return
     */
    public boolean lock(@NotNull String key, int second){
        return putData2RedisCache(key, second, CacheConstant.LOCK_VALUE);
    }
    /**
     * 设置过期时间
     * @param key
     * @param expTime
     */
    public void expireKey(@NotNull String key, int expTime){
        try {
            if(redisClusterClient.isExistByte(key))
                redisClusterClient.expireKeyByByte(key, expTime);
            if(redisClusterClient.isExistKey(key))
                redisClusterClient.expireKeyByStr(key, expTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将session从redis中获取出来
     * @param key
     * @return
     */
    public <T> T getSessionByFromRedis(@NotNull String key, Class<T> cls){
        return getCacheByFromRedis(key, cls);
    }
    public Object getSessionByFromRedis(@NotNull String key){
        return getCacheByFromRedis(key);
    }

    /**
     * 从一级缓存中读取并放入二级缓存中
     * @param key
     * @return
     */
    public <T> T getCacheByFromRedis(@NotNull String key, Class<T> cls){
        Object obj = getCacheByFromRedis(key);
        if(obj != null)
            return (T) obj;
        return null;
    }
    public Object getCacheByFromRedis(@NotNull String key){
        Object result = null;
        try{
            result = cache.get(key, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return redisClusterClient.getObjectFromRedis(key);
                }
            });
            return result;
        } catch (Exception e){
            e.printStackTrace();
            logger.error("redis获取key："+key+"异常\n"+e.getMessage());
        }
        return result;
    }

    /**
     * 从其他地方读取放入二级缓存中
     * @param key
     * @param call
     * @return
     */
    public <T> T getCacheByFromOther(@NotNull String key, Callable<Object> call, Class<T> cls){
        Object obj = getCacheByFromOther(key, call);
        if(obj != null)
            return (T) obj;
        return null;
    }
    public Object getCacheByFromOther(@NotNull String key, Callable<Object> call){
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
    public <T> T getCacheRedisFromOther(@NotNull String key, Callable<Object> call, Class<T> cls){
        Object obj = getCacheRedisFromOther(key, call);
        if(obj != null)
            return (T) obj;
        return null;
    }
    public Object getCacheRedisFromOther(@NotNull String key, Callable<Object> call){
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

    /**
     * 将全局资源提取出来
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T>T getResourceByFromRedis(@NotNull String key, Class<T> cls){
        return getCacheByFromRedis(key, cls);
    }
    public Object getResourceByFromRedis(@NotNull String key){
        return getCacheByFromRedis(key);
    }

    /**
     * 获取短暂时间的存储数据
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getCacheShortTimeByFromRedis(@NotNull String key, Class<T> cls){
        return getCacheByFromRedis(key, cls);
    }
    public Object getCacheShortTimeByFromRedis(@NotNull String key){
        return getCacheByFromRedis(key);
    }

    /**
     * 获取永久数据的存储数据
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getCacheForeverByFromRedis(@NotNull String key, Class<T> cls){
        return getCacheByFromRedis(key, cls);
    }
    public Object getCacheForeverByFromRedis(@NotNull String key){
        return getCacheByFromRedis(key);
    }

    public ByteArrayOutputStream getCacheByteByFromRedis(@NotNull String key){
        ByteArrayOutputStream baos = null;
        try {
            byte[] bytes = redisClusterClient.getByteFromRedis(key);
            if (bytes == null) return null;
            baos = new ByteArrayOutputStream();
            baos.write(bytes);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return baos;
    }

    /**
     * 获取累加器值
     * @param key
     * @return
     */
    public Long getIncKey(@NotNull String key){
        return redisClusterClient.getIncKey(key);
    }

    /**
     * 获取减数器值
     * @param key
     * @return
     */
    public Long getDecrBy(@NotNull String key){
        return redisClusterClient.getDecrBy(key);
    }

    /**
     * 累加器，加1
     * @param key
     * @return
     */
    public Long incKey(@NotNull String key){
        return redisClusterClient.incKey(key);
    }

    /**
     * 累加器，加value
     * @param key
     * @param vale
     * @return
     */
    public Long incKeyBy(@NotNull String key, long vale){
        return redisClusterClient.incKeyBy(key, vale);
    }

    /**
     * 累加器，加value
     * @param key
     * @param vale
     * @return
     */
    public Double incKeyBy(@NotNull String key, double vale){
        return redisClusterClient.incKeyBy(key, vale);
    }

    public Long incKeyByTime(@NotNull String key, int expireTime){
        Long l = incKey(key);
        try {
            redisClusterClient.expireKeyByStr(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public Long incKeyByTime(@NotNull String key, long value, int expireTime){
        Long l = incKeyBy(key, value);
        try {
             redisClusterClient.expireKeyByStr(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 减数器，减1
     * @param key
     * @return
     */
    public Long decrKey(@NotNull String key){
        return redisClusterClient.decrKey(key);
    }

    /**
     * 减数器，减value
     * @param key
     * @param value
     * @return
     */
    public Long decrKeyBy(@NotNull String key, long value){
        return redisClusterClient.decrKeyBy(key, value);
    }

    /**
     * 减数器，减value
     * @param key
     * @param value
     * @return
     */
    public Double decrKeyBy(@NotNull String key, double value){
        return incKeyBy(key, -1 * value);
    }


    public Long decrKeyByTime(@NotNull String key, int expireTime){
        Long l = decrKey(key);
        try {
            redisClusterClient.expireKeyByStr(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public Long decrKeyByTime(@NotNull String key, long value, int expireTime){
        Long l = decrKeyBy(key, value);
        try {
            redisClusterClient.expireKeyByStr(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 追加字符串，并返回
     * @param key
     * @param value
     * @return
     */
    public String append(@NotNull String key, String value){
        try {
            return redisClusterClient.append(key, value);
        } catch (Exception e) {
            logger.error("redis append error, cause:{}", e.getMessage());
            return null;
        }
    }

    public void invalidByRedis(@NotNull String key) {
        try{
            invalid(key);
            redisClusterClient.removeObject(key);
        } catch (Exception e){
            logger.error("redis remove error, cause:{}", e.getMessage());
        }
    }

    public long delByRedis(@NotNull String key){
        long value = 0;
        try{
            value = redisClusterClient.delKey(key);
        } catch (Exception e){
            logger.error("redis delKey error, cause:{}", e.getMessage());
        }
        return value;
    }

    /**
     * 判断是否存在，如果不获取具体信息的话，效率比get更快，根据
     * 具体业务来选择isExist或get
     * @param key
     * @return
     */
    public boolean isExistByRedis(@NotNull String key){
        try{
            if(!redisClusterClient.isExistByte(key)){
                return redisClusterClient.isExistKey(key);
            }
            return true;
        } catch (Exception e){
            logger.error("redis exist error, cause:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 将数据放入redis中，但这些数据不能重复（一般用于轨迹等其他不重复数据）
     * @param key
     * @param values
     */
    public void putData2RedisSet(String key, Object... values){
        try {
            redisClusterClient.saveObject2Set(key, values);
        } catch (Exception e ){
            logger.error("redis saveObject2Set error, cause:{}", e.getMessage());
        }
    }

    /**
     * 获取不重复数据集
     * @param key
     * @return
     */
    public <T> Set<T> getDataFromRedisSet(@NotNull String key, Class<T> cls){
        try{
            return redisClusterClient.getSet2Array(key, cls);
        } catch (Exception e){
            logger.error("redis getSetFromRedis error, cause:{}", e.getMessage());
            return null;
        }
    }

    public <T> Set<T> getDataFromRedisSetAndClearData(@NotNull String key, Class<T> cls){
        try{
            return redisClusterClient.getSet2Array(key, cls, true);
        } catch (Exception e){
            logger.error("redis getSetFromRedis error, cause:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 将key、value存入redis中的map
     * @param key
     * @param mapKey
     * @param mapValue
     */
    public void putData2RedisMap(String key, String mapKey, Object mapValue) {
        try{
            redisClusterClient.saveObject2Map(key, mapKey, mapValue);
        } catch (Exception e){
            logger.error("redis saveObject2Map error, cause:{}", e.getMessage());
        }
    }

    /**
     * 将map存入redis
     * @param key
     * @param map
     */
    public void putData2RedisMap(String key, Map<String, Object> map){
        try{
            redisClusterClient.saveObject2Map(key, map);
        } catch (Exception e){
            logger.error("redis saveObject2Map error, cause:{}", e.getMessage());
        }
    }

    /**
     * 从map中获取指定key的值
     * @param key
     * @param mapKey
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getDataFromRedisMap(String key, String mapKey, Class<T> cls){
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(mapKey)) return null;
        try{
            return redisClusterClient.getMapValueByKey(key, mapKey, cls);
        } catch (Exception e){
            logger.error("redis getMapFromRedis error, cause:{}", e.getMessage());
            return null;
        }
    }
    /**
     * 从map中删除指定key的值
     * @param key
     * @param mapKey
     * @return
     */
    public void delMapValueByKey(String key, String mapKey){
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(mapKey)) return;
        try{
            redisClusterClient.delMapValueByKey(key, mapKey);
        } catch (Exception e){
            logger.error("redis delMapValueByKey error, cause:{}", e.getMessage());
        }
    }


    public <T> T getDataFromMapWithLocal(String key, String mapKey, Class<T> clz) {
        Object result = null;
        try{
            result = cache.get(key + mapKey, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return redisClusterClient.getMapValueByKey(key, mapKey, clz);
                }
            });
        } catch (Exception e){
            logger.error("redis getDataFromMapWithLocal error, cause:{}", e.getMessage());
        }
        if (result == null)
            return null;
        return (T)result;
    }

    /**
     * 获取指定key的map
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getDataFromRedisMap(String key, Class<T> cls){
        if (StringUtils.isEmpty(key)) return null;
        try{
            return redisClusterClient.getMap(key, cls);
        } catch (Exception e){
            logger.error("redis getMapFromRedis error, cause:{}", e.getMessage());
            return null;
        }
    }

    public long getMapLeng(String key) {
        try{
            return redisClusterClient.getMapLenByKey(key);
        } catch (Exception e){
            logger.error("redis getMapLeng error, cause:{}", e.getMessage());
            return 0;
        }
    }

    /**
     * 判断是否存在mapkey
     * @param key
     * @param mapKey
     * @return
     */
    public boolean isExistInMap(String key, String mapKey){
        try{
            return redisClusterClient.isExistInMap(key, mapKey);
        } catch (Exception e){
            logger.error("redis isExistInMap, cause:{}", e.getMessage());
            return false;
        }
    }

    public void invalid(@NotNull String key){
        cache.invalidate(key);
    }

    public void invalidAll(){
        cache.invalidateAll();
    }

    public ConcurrentMap<String, Object> getAllCache(){
        return cache.asMap();
    }

    /**
     * 判断set中是否存在该object
     * @param key
     * @param obj
     * @return
     */
    public boolean isExistInSet(@NotNull String key, Object obj) {
        try {
            return redisClusterClient.isExistInSet(key, obj);
        } catch (Exception e){
            logger.error("redis isExistInSet, cause:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 移除set中得某些值
     * @param key
     * @param obj  为一个数组值，如果是list，set这些请转化为array
     * @return
     */
    public void removeObjectInSet(@NotNull String key, Object... obj) {
        try{
            redisClusterClient.removeObjectInSet(key, obj);
        } catch (Exception e){
            logger.error("redis removeObjectInSet");
        }
    }

    public void invalidConstantKey(@NotNull String key, @NotNull Object... values) {
        try{
            String redisKey = String.format(key, values);
            invalidByRedis(redisKey);
        }catch (Exception e){
            logger.error("invoke invalidConstantKey error, cause:{}", e.getMessage());
        }
    }

    public Set<String> keys(@NotNull String key) {
        try{
            return redisClusterClient.keys(key);
        }catch (Exception e){
            logger.error("invoke keys error, cause:{}", e.getMessage());
            return null;
        }
    }
    /******************************************  begin redis set deal ***************************************/

    public Long setScard(@NotNull String queueName){
        return redisClusterClient.scard(queueName);
    }

    public Object setPop(@NotNull String queueName, Class cls) throws ClassNotFoundException {
        try {
            return redisClusterClient.setPop(queueName, cls);
        } catch (IOException e) {
            logger.error("remove obj from list error");
        }
        return null;
    }
   /* public Set<Object> setPop(@NotNull String queueName, @NotNull Long size, Class cls) throws ClassNotFoundException {
        try {
            return redisClusterClient.setPop(queueName, size, cls);
        } catch (IOException e) {
            logger.error("remove obj from list error");
        }
        return null;
    }*/


    /******************************************  begin redis list deal ***************************************/
    /**
     * 添加到list的头部
     */
    public Long listAddToHead(@NotNull String queueName, @NotNull Object obj){
        try {
            return redisClusterClient.listAddToHead(queueName, obj);
        } catch (IOException e) {
            logger.error("add obj to list head error");
        }
        return 0l;
    }

    /**
     * 添加到list的头部
     */
    public void listAddToHead(@NotNull String queueName, @NotNull Object... objs) throws IOException {
        for(Object obj : objs) listAddToHead(queueName, obj);
        try {
            redisClusterClient.listAddToHead(queueName, objs);
        } catch (IOException e) {
            logger.error("add objs to list head error");
        }
    }




    /**
     * 删除
     * @param queueName
     * @param count =0时：全部删除与obj相等的；
     * @param obj
     * @return
     */
    public Long listRem(@NotNull String queueName, Long count, @NotNull Object obj){
        try {
            return redisClusterClient.listRem(queueName, count, obj);
        } catch (IOException e) {
            logger.error("remove obj from list error");
        }
        return 0l;
    }

    public void listRem(@NotNull String queueName, Long count, @NotNull Object... objs){
        try {
            redisClusterClient.listRem(queueName, count, objs);
        } catch (IOException e) {
            logger.error("remove objs from list error");
        }
    }

    public Integer listLen(@NotNull String queueName){
        try {
            Long res = redisClusterClient.listLen(queueName);
            if (res != null)
                return res.intValue();
            else
                return null;
        } catch (IOException e) {
            logger.error("get length from list error");
        }
        return null;
    }

    public <T> List<T> listGetByPage(@NotNull String queueName, Class<T> cls, Integer pageNo, Integer pageSize){
        if(pageNo == 0l || pageSize == 0l) return null;

        if(pageNo == null || pageSize == null){
            pageNo = 0;
            pageSize = -1;
        }else {
            pageNo = (pageNo - 1) * pageSize;
            pageSize = pageNo + pageSize;
        }

        try {
            return redisClusterClient.listGetByRange(queueName, cls, pageNo.longValue(), pageSize.longValue());
        } catch (Exception e) {
            logger.error("get by page from list error");
        }
        return null;
    }

    public <T> List<T> listGetByIndex(@NotNull String queueName, Class<T> cls, @NotNull Integer begin, @NotNull Integer end){
        try {
            return redisClusterClient.listGetByRange(queueName, cls, begin.longValue(), end.longValue());
        } catch (Exception e) {
            logger.error("get by index from list error");
        }
        return null;
    }

    public <T> List<T> listGetAll(@NotNull String queueName, Class<T> cls){
        try {
            return listGetByPage(queueName, cls, null, null);
        } catch (Exception e) {
            logger.error("get all from list error");
        }
        return null;
    }

    public static String buildRedisKey(String key, String value){
        if(StringUtils.isEmpty(value) || StringUtils.isEmpty(key)) return null;

        return String.format(key, value);
    }

    public Cache<String, Object> getCache() {
        return cache;
    }
}
