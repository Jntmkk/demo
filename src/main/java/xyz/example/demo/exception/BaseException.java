package xyz.example.demo.exception;

import xyz.example.demo.bean.BaseErrorInfoInterface;

public abstract class BaseException extends RuntimeException implements BaseErrorInfoInterface {
    protected String msg;
    protected String code;

    public BaseException() {
    }

    public BaseException(String msg, String code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    @Override
    public String getResultCode() {
        return code;
    }

    @Override
    public String getResultMsg() {
        return msg;
    }
}
