package ai.wbw.service.core.service;

import ai.wbw.service.core.dto.TokenModel;

/**
 * 对Token进行操作的接口
 */
public interface IRedisTokenService {

    /**
     * 创建一个App使用的token
     * @param userId 用户的id
     * @return 生成的token
     */
    public TokenModel createAppToken(String userId);

    /**
     * 创建一个web使用的token
     * @param userId 用户的id
     * @return 生成的token
     */
    public TokenModel createWebToken(String userId);

    /**
     * 检查token是否有效
     * @param token
     * @return 是否有效
     */
    public boolean checkToken(String token);

    /**
     * 从字符串中解析token
     * @param authentication 加密后的字符串
     * @return
     */
    public TokenModel getToken(String authentication);

    /**
     * 清除token
     * @param token 登录用户的id
     */
    public void deleteToken(String token);

}
