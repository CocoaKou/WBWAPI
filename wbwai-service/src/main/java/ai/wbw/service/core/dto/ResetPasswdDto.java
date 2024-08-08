package ai.wbw.service.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * 重置密码的参数对象
 */
public class ResetPasswdDto {

    @Email(message = "Invalid email address")
    private String username;
    @NotEmpty(message = "Password cannot be null")
    private String newPasswd;
    @NotEmpty(message = "Verification code cannot be null")
    private String verificationCode;

    public ResetPasswdDto() {
    }

    ;

    public ResetPasswdDto(@NotEmpty(message = "username cannot be null") String username, @NotEmpty(message = "password cannot be null") String newPasswd, @NotEmpty(message = "verification code cannot be null") String verificationCode) {
        this.username = username;
        this.newPasswd = newPasswd;
        this.verificationCode = verificationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public void setNewPasswd(String newPasswd) {
        this.newPasswd = newPasswd;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public String toString() {
        return "ResetPasswdDto{" +
                "username='" + username + '\'' +
                ", newPasswd='" + newPasswd + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
