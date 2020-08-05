package xyz.example.demo.exception;

import xyz.example.demo.bean.BaseErrorInfoInterface;
import xyz.example.demo.bean.CommonEnum;

public class FormatException extends BaseException {
    public FormatException() {
        this(CommonEnum.BODY_NOT_MATCH);
    }

    public FormatException(String msg, String code) {
        this.code = code;
        this.msg = msg;
    }

    public FormatException(BaseErrorInfoInterface errorInfo) {
        this.code = errorInfo.getResultCode();
        this.msg = errorInfo.getResultMsg();
    }
}
