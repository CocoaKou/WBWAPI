package ai.wbw.service.controller;

import ai.wbw.service.core.service.IUserService;
import ai.wbw.service.common.enums.Code;
import ai.wbw.service.common.model.ResultEntity;
import ai.wbw.service.core.entity.User;
import ai.wbw.service.core.dto.*;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

/**
 * @Description 主要流程接口Api
 */

@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Autowired
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    //测试接口
    @GetMapping(value="/app/test/hello")
    public @ResponseBody
    String hello() {
        return "Hello";
    }

    //测试接口
    @PostMapping(value="/app/test/verify")
    public @ResponseBody
    JSONObject testVerify(LoginReqDto loginReqDto) {
        return userService.testVerify(loginReqDto);
    }

    //用户注册
    @PostMapping(value="/app/user/register")
    public @ResponseBody
    ResultEntity<User> register(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        return userService.createUser(user);
    }

    //用户验证
    @PostMapping(value="/app/user/verify")
    public @ResponseBody ResultEntity<LoginResultDto> verify(@RequestBody User user) {
        if(user==null){
            return new ResultEntity<>(Code.FAILED.getValue(),"user not exist",null);
        }
        ResultEntity<LoginResultDto> result = userService.verify(user.getUsername(),user.getPassword());
        return result;
    }

    //用户验证
    @PostMapping(value="/app/user/logout")
    public @ResponseBody ResultEntity<String> logout(@RequestBody Map<String,Long> map) {
        if(map == null || !map.containsKey("userId")){
            return new ResultEntity<String>(Code.FAILED.getValue(), "userId null","");
        }
        ResultEntity<String> result = userService.logout(map.get("userId"));
        return result;
    }

    //根据用户名查询
    @PostMapping(value="/app/user/queryByUsername")
    public @ResponseBody ResultEntity<User> queryByUsername(@RequestBody UserDto userDto) {
        return userService.queryByUsername(userDto.getUsername());
    }

    //修改密码
    @PostMapping(value="/app/user/updatePwd")
    public @ResponseBody ResultEntity<User> updatePwd(@RequestBody UpdatePasswordDto updatePasswordDto) {
        return userService.updatePwd(updatePasswordDto.getUsername(), updatePasswordDto.getPassword(), updatePasswordDto.getNewPassword());
    }

    @PostMapping(value="/app/user/resetPwd")
    public @ResponseBody ResultEntity<String> resetPwd(@RequestBody @Valid ResetPasswdDto resetPasswdDto) {
        return userService.resetPwd(resetPasswdDto);
    }
}