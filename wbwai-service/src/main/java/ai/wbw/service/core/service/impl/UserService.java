package ai.wbw.service.core.service.impl;

import ai.wbw.service.util.JsonUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ai.wbw.service.common.constant.Constants;
import ai.wbw.service.common.constant.RedisConstants;
import ai.wbw.service.common.enums.Code;
import ai.wbw.service.common.model.ResultEntity;
import ai.wbw.service.util.CryptoUtil;
import ai.wbw.service.util.StringUtil;
import ai.wbw.service.core.mapper.*;
import ai.wbw.service.core.entity.*;
import ai.wbw.service.core.service.*;
import ai.wbw.service.core.dto.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.StringUtils.hasText;

/**
 * @Description 用户相关的实现类
 */
@Service
@Transactional
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IRedisService redisService;

    @Autowired
    private IRedisTokenService redisTokenService;

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public ResultEntity<User> createUser(User user) {
        ResultEntity<User> re = new ResultEntity<User>(Code.FAILED.getValue(),"",null);
        if(user == null){
            re.setCode(Code.FAILED.getValue());
            re.setMsg("params null");
            return re;
        }
        if(StringUtils.isBlank(user.getUsername())|| StringUtils.isBlank(user.getPassword())){
            re.setCode(Code.FAILED.getValue());
            re.setMsg("username or passwd null");
            return re;
        }
        //未激活
        user.setState(Constants.USER_STATE_NOT_ACTIVATED);
        //加盐
        String salt = RandomStringUtils.randomAlphabetic(6);
        user.setSalt(salt);
        user.setPassword(CryptoUtil.encodeByMD5Base64(user.getPassword() + salt));
        User u = new User();
        u.setUsername(user.getUsername());
        //判断用户名是否已经存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.setEntity(u);
        User existUser = this.baseMapper.selectOne(wrapper);
        if(existUser != null){
            //如果未激活，且激活邮件失效，则允许重新注册
            if(Constants.USER_STATE_NOT_ACTIVATED.equals(existUser.getState()) && !redisService.existsKey(RedisConstants.REDIS_KEY_PREFIX_EMAIL + existUser.getId())){
                user.setId(existUser.getId());
                int flag = this.baseMapper.updateById(user);
                if(flag>0){
                    re.setCode(Code.SUCCESS.getValue());
                    re.setMsg(Code.SUCCESS.getMsg());
                    //发送激活邮件
                    //...
                }
                return re;
            }
            re.setCode(Code.FAILED.getValue());
            re.setMsg("user already exist");
            return re;
        }
        int flag = this.baseMapper.insert(user);
        if(flag > 0){
            re.setCode(Code.SUCCESS.getValue());
            re.setMsg(Code.SUCCESS.getMsg());
            //发送激活邮件
            //...
        }
        return re;
    }


    @Override
    public ResultEntity<LoginResultDto> verify(String username, String password) {
        ResultEntity<LoginResultDto> re = new ResultEntity<LoginResultDto>(Code.FAILED.getValue(),"",null);
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            re.setCode(Code.FAILED.getValue());
            re.setMsg("params null");
            return re;
        }
        User user = new User();
        user.setUsername(username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.setEntity(user);
        User existUser = this.baseMapper.selectOne(wrapper);
        if((existUser == null)){
            re.setMsg("user not exist");
            re.setCode(Code.FAILED.getValue());
            return re;
        }else {
            if(!CryptoUtil.encodeByMD5Base64(password + existUser.getSalt()).equals(existUser.getPassword())){
                re.setMsg("username or password error");
            }else if(Constants.USER_STATE_NOT_ACTIVATED.equals(existUser.getState())){
                re.setMsg("account not actived");
                return re;
            }else if(Constants.USER_STATE_DISABLED.equals(existUser.getState())){
                re.setMsg("account disabled");
                return re;
            }else {
                LoginResultDto<LoginResponse> loginResultDto = new LoginResultDto<LoginResponse>();
                TokenModel token = redisTokenService.createAppToken(existUser.getId() + "");
                loginResultDto.setToken(token.getToken());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUserId(Long.valueOf(existUser.getId()));
                loginResponse.setUsername(existUser.getUsername());
                loginResponse.setNickname(existUser.getNickname());
                if(hasText(existUser.getAvatar())) {
                    loginResponse.setAvatar("avatar url");
                }
                loginResultDto.setResult(loginResponse);
                re.setCode(Code.SUCCESS.getValue());
                re.setMsg(Code.SUCCESS.getMsg());
                re.setData(loginResultDto);
            }
        }
        return re;
    }

    @Override
    public ResultEntity<User> queryByUsername(String username) {
        ResultEntity<User> re = new ResultEntity<User>(Code.FAILED.getValue(),"user not exist",null);
        if(StringUtils.isBlank(username)){
            re.setCode(Code.FAILED.getValue());
            re.setMsg("param null");
            return re;
        }
        User u = new User();
        u.setUsername(username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.setEntity(u);
        User user = this.baseMapper.selectOne(wrapper);
        if(user != null){
            re.setCode(Code.SUCCESS.getValue());
            re.setData(user);
        }
        return re;
    }

    @Override
    public ResultEntity<User> updatePwd(String username, String password, String newPassword) {
        ResultEntity<User> re = new ResultEntity<User>(Code.FAILED.getValue(),"",null);
        if(StringUtil.isBlank(username) || StringUtil.isBlank(password) || StringUtil.isBlank(newPassword)){
            re.setCode(Code.FAILED.getValue());
            re.setMsg("param null");
            return re;
        }
        User user = new User();
        user.setUsername(username);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.setEntity(user);
        User u = this.baseMapper.selectOne(wrapper);
        if(u==null){
            re.setCode(Code.FAILED.getValue());
            re.setMsg("username not exist");
            return re;
        }
        if(CryptoUtil.validateByMD5Base64(u.getPassword(),password + u.getSalt())){
            u.setPassword(CryptoUtil.encodeByMD5Base64(newPassword + u.getSalt()));
            this.baseMapper.updateById(u);
            re.setCode(Code.SUCCESS.getValue());
            re.setMsg(Code.SUCCESS.getMsg());
            re.setData(u);
        }else{
            re.setCode(Code.FAILED.getValue());
            re.setMsg("username or passwd error");
        }
        return re;
    }

    @Override
    public ResultEntity<String> resetPwd(ResetPasswdDto resetPasswdDto) {
        ResultEntity<String> re = new ResultEntity<String>(Code.FAILED.getValue(), "", "");
        User u = new User();
        u.setUsername(resetPasswdDto.getUsername());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.setEntity(u);
        User user = this.baseMapper.selectOne(wrapper);
        if (user == null) {
            re.setCode(Code.FAILED.getValue());
            re.setMsg("user not exist");
            return re;
        }
        String resetPasswdEmailCodeRedisKey = RedisConstants.REDIS_KEY_PREFIX_CAPTCHA + resetPasswdDto.getUsername();
        if(redisService.existsKey(resetPasswdEmailCodeRedisKey) && redisService.getValue(resetPasswdEmailCodeRedisKey).equals(resetPasswdDto.getVerificationCode())){
            String newEncodePassword = CryptoUtil.encodeByMD5Base64(resetPasswdDto.getNewPasswd() + user.getSalt());
            if(newEncodePassword.equals(user.getPassword())){
                re.setCode(Code.FAILED.getValue());
                re.setMsg("new password is same as old password");
                return re;
            }
            //修改为新密码
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username", resetPasswdDto.getUsername());
            updateWrapper.set("password", newEncodePassword);
            if(this.update(null,updateWrapper)){
                redisService.deleteKey(resetPasswdEmailCodeRedisKey);
                re.setMsg(Code.SUCCESS.getMsg());
                re.setCode(Code.SUCCESS.getValue());
                return re;
            }
        }else{
            re.setCode(Code.FAILED.getValue());
            re.setMsg("verification code invalid");
        }
        return re;
    }

    @Override
    public ResultEntity<String> logout(Long userId) {
        if(userId == null){
            return new ResultEntity<>(Code.FAILED.getValue(), "userId null", "");
        }
        //删除相关的token和regid
        redisService.deleteKey(RedisConstants.REDIS_KEY_PREFIX_APP + userId);
        return new ResultEntity<>(Code.SUCCESS.getValue(), Code.SUCCESS.getMsg(), "");
    }

    @Override
    public JSONObject testVerify(LoginReqDto loginReqDto){
        return JsonUtils.readJsonFile("templates/json/verify.json");
    }
}
