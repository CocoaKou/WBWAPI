package ai.wbw.service.core.dto;

import jakarta.validation.constraints.NotEmpty;

/**
 * 用户数据对象
 */
public class LoginReqDto {

    @NotEmpty(message = "username cannot be null")
    private String username;
    @NotEmpty(message = "password cannot be null")

    private String password;

    public LoginReqDto(){};

    public LoginReqDto(@NotEmpty(message = "username cannot be null") String username, @NotEmpty(message = "password cannot be null") String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginReqDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
