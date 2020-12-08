package xyz.example.demo.config;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.web3j.protocol.exceptions.TransactionException;
import xyz.example.demo.bean.BizException;
import xyz.example.demo.bean.CommonEnum;
import xyz.example.demo.bean.ResponseResult;
import xyz.example.demo.exception.FormatException;
import xyz.example.demo.exception.UserAlreadyExistsException;
import xyz.example.demo.utils.GlobalExceptionUtil;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "xyz.example.demo.controller")
public class BaseGlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (!(o instanceof ResponseResult)) {
            ResponseResult responseResult = ResponseResult.success(o);
            if (o instanceof String)
                return JSON.toJSONString(responseResult);
            return responseResult;
        }
        return o;
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseBody
    public ResponseResult userAlreadyExistsException(HttpServletRequest req, UserAlreadyExistsException e) {
        return ResponseResult.error(e);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseResult runtimeException(HttpServletRequest req, RuntimeException e) {
        e.printStackTrace();
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler(value = FormatException.class)
    @ResponseBody
    public ResponseResult formatExceptionHandler(HttpServletRequest req, FormatException e) {
        return ResponseResult.error(e);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult formatExceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        return ResponseResult.error(GlobalExceptionUtil.convertExceptionToReadableString(e));
    }

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResponseResult bizExceptionHandler(HttpServletRequest req, BizException e) {
        return ResponseResult.error(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = TransactionException.class)
    @ResponseBody
    public ResponseResult transactionException(HttpServletRequest req, TransactionException e) {
        return ResponseResult.error(e.getMessage());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest req, NullPointerException e) {
        e.printStackTrace();
        return ResponseResult.error(CommonEnum.BODY_NOT_MATCH);
    }


    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest req, Exception e) {
        return ResponseResult.error("500", e.getMessage());
    }
}
