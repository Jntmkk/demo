package xyz.example.demo.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseResult {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应结果
     */
    private Object data;

    public ResponseResult() {
    }

    public ResponseResult(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.msg = errorInfo.getResultMsg();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }



    /**
     * 成功
     *
     * @return
     */
    public static ResponseResult success() {
        return success(null);
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static ResponseResult success(Object data) {
        ResponseResult rb = new ResponseResult();
        rb.setCode(CommonEnum.SUCCESS.getResultCode());
        rb.setMessage(CommonEnum.SUCCESS.getResultMsg());
        rb.setData(data);
        return rb;
    }

    /**
     * 失败
     */
    public static ResponseResult error(BaseErrorInfoInterface errorInfo) {
        ResponseResult rb = new ResponseResult();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setData(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResponseResult error(String code, String msg) {
        ResponseResult rb = new ResponseResult();
        rb.setCode(code);
        rb.setMessage(msg);
        rb.setData(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResponseResult error( String msg) {
        ResponseResult rb = new ResponseResult();
        rb.setCode("-1");
        rb.setMessage(msg);
        rb.setData(null);
        return rb;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
