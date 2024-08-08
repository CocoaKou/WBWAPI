package ai.wbw.service.common.constant;

/**
 * @Description Redis常量类
 * @Version 1.0
 */
public class RedisConstants {

    /** 暂无过期时间*/
    public static final Integer REDIS_EXPIRE_NULL = -1;

    /** App redis key 前缀*/
    public static final String REDIS_KEY_PREFIX_APP = "APP_";

    /** WEB redis key 前缀*/
    public static final String REDIS_KEY_PREFIX_WEB = "WEB_";

    /** 邮箱账号激活 redis key 前缀*/
    public static final String REDIS_KEY_PREFIX_EMAIL = "EMAIL_";

    /** 邮箱账号激活 redis key 前缀*/
    public static final String REDIS_KEY_PREFIX_CAPTCHA = "CAPTCHA_";

    /**redis token APP 过期时间:单位小时*/
    public static final int REDIS_TOKEN_EXPIRE_TIME_APP = 7 * 24;

    /**redis token WEB 过期时间:单位小时*/
    public static final int REDIS_TOKEN_EXPIRE_TIME_WEB = 2;

    /** 创建的token自身的过期时间:单位天*/
    public static final int CREATE_TOKEN_EXPIRE_TIME = 3650;


}
