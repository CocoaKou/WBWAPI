package ai.wbw.service.core.dto;

public class LoginResultDto<T> {

    private String token;

    private T result;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LoginResultDto{" +
                "token='" + token + '\'' +
                ", result=" + result +
                '}';
    }
}
