package ai.wbw.service.core.service.impl;

import ai.wbw.service.common.constant.RedisConstants;
import ai.wbw.service.util.JwtTokenUtil;
import ai.wbw.service.core.service.IRedisTokenService;
import ai.wbw.service.core.dto.TokenModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 */
@Service
public class RedisTokenService implements IRedisTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public TokenModel createAppToken(String userId) {
        //使用uuid作为源token
        String token = "";
        try {
            token = JwtTokenUtil.createToken(userId, RedisConstants.CREATE_TOKEN_EXPIRE_TIME * 60 * 60 * 24);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TokenModel model = new TokenModel(userId, token);
        //System.out.println(token);
        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(RedisConstants.REDIS_KEY_PREFIX_APP + userId).set(token, RedisConstants.REDIS_TOKEN_EXPIRE_TIME_APP, TimeUnit.HOURS);
        return model;
    }

    public TokenModel createWebToken(String userId) {
        //使用uuid作为源token
        String token = "";
        try {
            token = JwtTokenUtil.createToken(userId, RedisConstants.CREATE_TOKEN_EXPIRE_TIME * 60 * 60 * 24);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TokenModel model = new TokenModel(userId, token);
        //System.out.println(token);
        //存储到redis并设置过期时间
        redisTemplate.boundValueOps(RedisConstants.REDIS_KEY_PREFIX_WEB + userId).set(token, RedisConstants.REDIS_TOKEN_EXPIRE_TIME_WEB, TimeUnit.HOURS);
        return model;
    }

    public TokenModel getToken(String authentication) {
        if (null == authentication || authentication.length() == 0) {
            return null;
        }

        String token = authentication;
        String userId = "";
        try {
            userId = JwtTokenUtil.getAppUID(token);
            //System.out.println("-------------userId:"+userId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new TokenModel(userId, token);
    }

    public boolean checkToken(String token){
        if (StringUtils.isBlank(token)) {
            return false;
        }
        if(redisTemplate.hasKey(token)){
            try {
                JwtTokenUtil.verifyToken(token);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
            redisTemplate.boundValueOps(token).expire(RedisConstants.REDIS_TOKEN_EXPIRE_TIME_APP, TimeUnit.HOURS);
            return true;
        }
        return false;
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }

}

