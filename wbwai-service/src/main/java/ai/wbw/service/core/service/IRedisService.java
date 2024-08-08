package ai.wbw.service.core.service;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description Redis接口类
 * @Version 1.0
 */
public interface IRedisService {

    boolean existsKey(String key);
    void renameKey(String oldKey, String newKey);
    boolean renameKeyNotExist(String oldKey, String newKey);
    void deleteKey(String key);
    void deleteKey(String... keys);
    void deleteKey(Collection<String> keys);
    void expireKey(String key, long time, TimeUnit timeUnit);
    void expireKeyAt(String key, Date date);
    long getKeyExpire(String key, TimeUnit timeUnit);
    void persistKey(String key);
    void addKey(String key, String value);
    void addKey(String key, String value, long expireTime);
    void addKey(String key, String value, long expireTime, TimeUnit timeUnit);
    String getValue(String key);

    void updateValue(String key, String value);
    void updateValue(String key, String value, long expireTime);
}
