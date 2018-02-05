package com.wxdmi.utils.cache;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wxdmi.utils.io.CloserUtil;
import com.wxdmi.utils.io.ConnectionIO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by on 15/6/15.
 */
public class RedisClusterClient implements ConnectionIO {

    public static final Logger logger = LoggerFactory.getLogger(RedisClusterClient.class);

    private RedisConfig redisConfig;
    private JedisCluster cluster;
    private Set<HostAndPort> clusterNodes;
    private Charset defaultChartset = Charset.forName("UTF-8");

    @NotNull
    private String ipArr;
    @NotNull
    private Integer port;
    @NotNull
    private Integer timeout;

    public String getIpArr() {
        return ipArr;
    }

    public void setIpArr(String ipArr) {
        this.ipArr = ipArr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    @Override
    public void init() {
        clusterNodes = Sets.newHashSet();
        String[] ips = ipArr.split(",");
        String[] ipArr = null;
        for(String ip : ips){
            ipArr = ip.split(":");
            clusterNodes.add(new HostAndPort(ipArr[0], Integer.parseInt(ipArr[1])));
        }
        connect();
    }

    @Override
    public boolean connect() {
        if(clusterNodes != null && !clusterNodes.isEmpty()) {
            cluster = new JedisCluster(clusterNodes, timeout, redisConfig.getJedisPoolConfig());
            logger.info("connect redis pool success");
            return true;
        }
        return false;
    }

    @Override
    public boolean disconnect() {
        if(cluster != null){
            try {
                cluster.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("disconnect redis pool success");
            return true;
        }
        return false;
    }

    public void persitenceObject(String key, int deadLineSeconds, Object object) throws IOException{
        persitenceObjectSerializable(key, deadLineSeconds, object);
    }

    public void persitenceObjectByShortTime(String key, Object object) throws IOException {
        persitenceObjectSerializable(key, redisConfig.getCacheShortTime(), object);
    }

    public void persitenceObject(String key, Object object) throws IOException {
        persitenceObjectSerializable(key, redisConfig.getCacheDeadlineTime(), object);
    }

    public void persitenceSessionObject(String key, Object session) throws IOException {
        persitenceObjectSerializable(key, redisConfig.getCacheSessionDeadLineTime(), session);
    }

    public void persitenceResource(String key, Object object) throws IOException {
        persitenceObjectSerializable(key, redisConfig.getCacheResoureDeadlintTime(), object);
    }

    public void persitenceObjectByForever(String key, Object object) throws IOException {
        persitenceObjectSerializable(key, redisConfig.getCacheForever(), object);
    }

    public void persitenceByte(String key, int deadLineSecondes, byte[] buffer) throws IOException{
        cluster.setex(key.getBytes(defaultChartset), deadLineSecondes, buffer);
    }

    public void removeObject(String key) throws IOException {
        if(cluster.exists(key.getBytes(defaultChartset)))
            cluster.del(key.getBytes(defaultChartset));
    }

    /**
     * 可以直接序列化的保存
     * @param key
     * @param object
     * @throws IOException
     */
    private void persitenceObjectSerializable(String key, int deadLineSeconds, Object object) throws IOException {
        cluster.setex(key.getBytes(defaultChartset), deadLineSeconds, serializableObject(object));
    }


    public Long persitenceObjectWithoutTime(String key, Object object) throws IOException {
        return cluster.setnx(key.getBytes(defaultChartset), serializableObject(object));
    }

    private byte[] serializableObject(Object object) throws IOException {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream out = null;
        try{
            baos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            return baos.toByteArray();
        } finally {
            CloserUtil.closeIO(out);
            CloserUtil.closeIO(baos);
        }
    }

    private Object unSerializableObject(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream bais = null;
        ObjectInputStream input = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            input = new ObjectInputStream(bais);
            return input.readObject();
        } finally {
            CloserUtil.closeIO(input);
            CloserUtil.closeIO(bais);
        }
    }

    /**
     * 累加器
     * @param key
     * @return
     */
    public Long incKey(String key){
        return cluster.incr(key);
    }

    /**
     * 获取累加器值
     * @param key
     * @return
     */
    public Long getIncKey(String key){
        String tmp = cluster.get(key);
        if(StringUtils.isEmpty(tmp))
            return 0l;
        return Long.parseLong(tmp);
    }

    /**
     * 获取减数器值
     * @param key
     * @return
     */
    public Long getDecrBy(String key){
        String tmp = cluster.get(key);
        if(StringUtils.isEmpty(tmp))
            return 0l;
        return Long.parseLong(tmp);
    }

    /**
     * 累加器，累加count
     * @param key
     * @param count
     * @return
     */
    public Long incKeyBy(String key, long count){
        return cluster.incrBy(key, count);
    }

    /**
     * 累加器，累加count
     * @param key
     * @param count
     * @return
     */
    public Double incKeyBy(String key, double count){
        return cluster.incrByFloat(key, count);
    }

    /**
     * 减数器
     * @param key
     * @return
     */
    public Long decrKey(String key){
        return cluster.decr(key);
    }
    /**
     * 减数器，递减count
     * @param key
     * @param count
     * @return
     */
    public Long decrKeyBy(String key, long count){
        return cluster.decrBy(key, count);
    }

    /**
     * 追加字符串，并返回
     * @param key
     * @param value
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String append(String key, String value) throws IOException, ClassNotFoundException {
        Long l = cluster.append(key.getBytes(defaultChartset), value.getBytes(defaultChartset));
        if(l != null && l > 0){
            return getStringFromRedis(key);
        }
        return null;
    }

    /**
     * 判断redis中是否存在
     * @param key
     * @return
     * @throws Exception
     */
    public boolean isExistByte(String key) throws Exception {
        return cluster.exists(key.getBytes(defaultChartset));
    }

    public boolean isExistKey(String key) throws Exception {
        return cluster.exists(key);
    }

    public void expireKeyByStr(String key, int second) throws Exception {
        cluster.expire(key, second);
    }

    public void expireKeyByByte(String key, int second) throws Exception {
        cluster.expire(key.getBytes(defaultChartset), second);
    }
    public long getExpireTime(String key) throws Exception {
        return cluster.ttl(key.getBytes(defaultChartset));
    }

    public Long delKey(String key) throws Exception {
        return cluster.del(key);
    }

    /**
     * 从redis里获取
     * @param key
     * @param call
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public String getStringFromRedis(String key, Callable<String> call)  throws Exception {
        String entity = getStringFromRedis(key);
        if(null == entity){
            return call.call();
        }
        return entity;
    }

    public String getStringFromRedis(String key) throws IOException, ClassNotFoundException {
        Object obj = getObjectFromRedis(key);
        if(obj != null)
            return String.valueOf(obj);
        return null;
    }

    public Object getObjectFromRedis(String key) throws IOException, ClassNotFoundException {
        byte[] buffer = cluster.get(key.getBytes(defaultChartset));
        if(buffer != null && buffer.length > 0)
            return unSerializableObject(buffer);
        return null;
    }

    public Object getObjectFromRedis(String key, Callable<Object> call) throws Exception {
        Object object = getObjectFromRedis(key);
        if(null == object)
            return call.call();
        return object;
    }

    public byte[] getByteFromRedis(String key) throws IOException, ClassNotFoundException {
        return cluster.get(key.getBytes(defaultChartset));
    }

    /**
     * 清除该队列
     * @param queueName 	队列名
     */
    public void clearQueue(String queueName){
        cluster.del(queueName);
    }

    /**
     * 压入队列
     * @param queueName 	队列名
     * @param msg			消息体
     */
    public void pushMsg(String queueName, String msg){
        // 如果队列不存在，说明该队列暂无持久化；如果存在则已持久化
        // 将队列持久化
        if(!cluster.exists(queueName))
            cluster.persist(queueName);
        cluster.lpush(queueName, msg);
    }

    /**
     * 将map存入redis
     * @param key
     * @param map
     * @throws Exception
     */
    public void saveObject2Map(String key, Map<String, Object> map) throws Exception {
        if(map != null && !map.isEmpty()){
            Map<byte[], byte[]> tmp = Maps.newHashMap();
            for(Map.Entry<String, Object> entry : map.entrySet()){
                tmp.put(entry.getKey().getBytes(defaultChartset), serializableObject(entry.getValue()));
            }
            cluster.hmset(key.getBytes(defaultChartset), tmp);
        }
    }

    /**
     * 将key、value存入redis中的map
     * @param key
     * @param mapKey
     * @param mapValue
     * @throws Exception
     */
    public void saveObject2Map(String key, String mapKey, Object mapValue) throws Exception {
        cluster.hset(key.getBytes(defaultChartset), mapKey.getBytes(defaultChartset), serializableObject(mapValue));
    }

    /**
     * 从map中获取指定key的值
     * @param key
     * @param mapKey
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T getMapValueByKey(String key, String mapKey, Class<T> cls) throws Exception {
        byte[] buffer = cluster.hget(key.getBytes(defaultChartset), mapKey.getBytes(defaultChartset));
        if(buffer != null && buffer.length > 0){
            return (T)unSerializableObject(buffer);
        }
        return null;
    }
    /**
     * 从map中删除指定key的值
     * @param key
     * @param mapKey
     * @return
     * @throws Exception
     */
    public void delMapValueByKey(String key, String mapKey) throws Exception {
        if(cluster.hexists(key.getBytes(defaultChartset), mapKey.getBytes(defaultChartset)))
            cluster.hdel(key.getBytes(defaultChartset), mapKey.getBytes(defaultChartset));
    }

    /**
     * 获取指定key的map
     * @param key
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> Map<String, T> getMap(String key, Class<T> cls) throws Exception {
        Map<byte[], byte[]> map = cluster.hgetAll(key.getBytes(defaultChartset));
        if(map != null && !map.isEmpty()){
            Map<String, T> tmp = Maps.newHashMap();
            for(Map.Entry<byte[], byte[]> entry : map.entrySet()){
                tmp.put(new String(entry.getKey(), defaultChartset), (T)unSerializableObject(entry.getValue()));
            }
            return tmp;
        }
        return null;
    }

    /**
     * 判断map中是否存在对应的key
     * @param key
     * @param mapKey
     * @return
     * @throws Exception
     */
    public boolean isExistInMap(String key, String mapKey) throws Exception {
        return cluster.hexists(key.getBytes(defaultChartset), mapKey.getBytes(defaultChartset));
    }

    /**
     * 获取map的大小
     * @param key
     * @return
     * @throws Exception
     */
    public long getMapLenByKey(String key) throws Exception {
        return cluster.hlen(key);
    }

    /**
     * 将无重复的数据压入集合中
     * @param key
     * @param objs
     * @throws Exception
     */
    public void saveObject2Set(String key, Object... objs) throws Exception {
        for(Object obj : objs){
            saveObject2Set(key, obj);
        }
    }

    /**
     * 将数据压入无重复的集合中
     * @param key
     * @param obj
     * @throws Exception
     */
    public void saveObject2Set(String key, Object obj) throws Exception {
        cluster.sadd(key.getBytes(defaultChartset), serializableObject(obj));
    }

    /**
     * 判断set中是否存在该object
     * @param key
     * @param obj
     * @return
     * @throws Exception
     */
    public boolean isExistInSet(String key, Object obj) throws Exception {
        return cluster.sismember(key.getBytes(defaultChartset), serializableObject(obj)).booleanValue();
    }

    /**
     * 将set中移除对应obj
     * @param key
     * @param objs
     * @return
     * @throws Exception
     */
    public void removeObjectInSet(String key, Object... objs) throws Exception {
        for(Object obj : objs){
            removeObjectInSet(key, obj);
        }
    }
    public long removeObjectInSet(String key, Object obj) throws Exception {
        return cluster.srem(key.getBytes(defaultChartset), serializableObject(obj));
    }

    /**
     * 得到无重复的集合
     * @param key
     * @return
     * @throws Exception
     */
    public <T> Set<T> getSet2Array(String key, Class<T> cls) throws Exception {
        Set<byte[]> result = cluster.smembers(key.getBytes(defaultChartset));
        if(result != null){
            Set<T> set = Sets.newHashSet();
            for(byte[] buff : result){
                set.add((T)unSerializableObject(buff));
            }
            return set;
        }
        return null;
    }

    public <T> Set<T> getSet2Array(String key, Class<T> cls, boolean bClearData) throws Exception {
        Set<T> result = null;
        ReentrantLock lock = new ReentrantLock();
        try{
            lock.lock();
            result = getSet2Array(key, cls);
            if(bClearData)
                cluster.del(key.getBytes(defaultChartset));
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * 队列提出
     * @param queueName 	队列名
     * @return             	返回消息体
     */
    public String popMsg(String queueName){
        return cluster.rpop(queueName);
    }

    /**
     * keys
     * @return             	返回keys
     */
    public TreeSet<String> keys(String pattern){
        TreeSet<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = cluster.getClusterNodes();
        for(String k : clusterNodes.keySet()){
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(pattern));
            } catch(Exception e){
                logger.error("Getting keys error: {}", e);
            } finally{
                connection.close();//用完一定要close这个链接！！！
            }
        }
        return keys;
    }

    /******************************************  begin redis set deal ***************************************/

    public <T> T setPop(String queueName, Class<T> cls) throws IOException, ClassNotFoundException {
        byte[] buffer = cluster.spop(queueName.getBytes(defaultChartset));

        if(buffer != null && buffer.length > 0){
            return (T)unSerializableObject(buffer);
        }

        return null;
    }

    public Long scard(String queueName) {
        return cluster.scard(queueName.getBytes(defaultChartset));
    }

    /*public <T> Set<T> setPop(String queueName, long size,  Class<T> cls) throws IOException, ClassNotFoundException {
        Set<byte[]> result = cluster.spop(queueName.getBytes(defaultChartset), size);

        if(result != null){
            Set<T> set = Sets.newHashSet();
            for(byte[] buff : result){
                set.add((T)unSerializableObject(buff));
            }
            return set;
        }
        return null;
    }*/

    /******************************************  begin redis list deal ***************************************/

    public Long listAddToHead(String queueName, Object obj) throws IOException {
        return cluster.lpush(queueName.getBytes(defaultChartset), serializableObject(obj));
    }

    public void listAddToHead(String queueName, Object... objs) throws IOException {
        for(Object obj : objs) listAddToHead(queueName, obj);
    }

    public Long listRem(String queueName, Long count, Object obj) throws IOException {
        // 删除所有与obj相等的对象
        if(count ==  null) count = 0l;

        return cluster.lrem(queueName.getBytes(defaultChartset), count, serializableObject(obj));
    }


    public void listRem(String queueName, Long count, Object... objs) throws IOException {
        for(Object obj : objs) listRem(queueName, count, obj);
    }

    public Long listLen(String queueName) throws IOException {
        return cluster.llen(queueName.getBytes(defaultChartset));
    }

    public <T> List<T> listGetByRange(String queueName, Class<T> cls, Long begin, Long end) throws IOException, ClassNotFoundException {
        List<byte[]> list = cluster.lrange(queueName.getBytes(defaultChartset), begin, end);
        if(CollectionUtils.isEmpty(list)) return null;

        List<T> result = new ArrayList<T>();

        for(byte[] bytes : list){
            result.add((T)unSerializableObject(bytes));
        }

        return result;
    }
}
