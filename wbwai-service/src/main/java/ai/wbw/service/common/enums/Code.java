package ai.wbw.service.common.enums;

public enum Code {
    FAILED(9999,"failed"),
    SUCCESS(1000,"success");

    private int value;
    private String msg;

    Code(int value, String msg){
        this.value = value;
        this.msg = msg;
    }
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
