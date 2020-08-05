package xyz.example.demo.utils;

import org.springframework.web.bind.MethodArgumentNotValidException;

public class GlobalExceptionUtil {
    public static String convertExceptionToReadableString(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldError().getDefaultMessage();
    }
}
