package ai.wbw.service.common.model;

import java.io.Serializable;

/**
 * @Description 结果List封装实体
 */
public class ResultList<T> implements Serializable {
    private static final long serialVersionUID = -4444889875087130606L;
    private long timestamp;	//服务器时间戳
    private long total;	//所有记录总数
    private T info;	//分类信息字段


    public ResultList(){
    }

    public ResultList(long total, long timetamp, T data) {
        this.total = total;
        this.timestamp = timetamp;
        this.info = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getInfo(){
        return info;
    }

    public  void setInfo(T info){
        this.info = info;
    }
}
