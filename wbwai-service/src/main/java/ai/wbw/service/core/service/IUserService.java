package ai.wbw.service.core.service;

import ai.wbw.service.common.model.ResultEntity;
import ai.wbw.service.core.dto.LoginReqDto;
import ai.wbw.service.core.dto.LoginResultDto;
import ai.wbw.service.core.dto.ResetPasswdDto;
import ai.wbw.service.core.dto.UserDto;
import ai.wbw.service.core.entity.User;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description 用户相关接口
 */
public interface IUserService extends IService<User> {

    ResultEntity<User> createUser(User user);

    /**
     * 校验密码
     * @param username
     * @param password
     * @return
     */
    ResultEntity<LoginResultDto> verify(String username, String password);

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    ResultEntity<User> queryByUsername(String username);

    /**
     * 修改密码
     * @param username
     * @param password
     * @param newPasswd
     * @return
     */
    ResultEntity<User> updatePwd(String username, String password, String newPasswd);

    /**
     * 重置密码
     * @return
     */
    ResultEntity<String> resetPwd(ResetPasswdDto resetPasswdDto);

    /**
     * 登出
     * @param userId
     * @return
     */
    ResultEntity<String> logout(Long userId);

    JSONObject testVerify(LoginReqDto loginReqDto);
}
