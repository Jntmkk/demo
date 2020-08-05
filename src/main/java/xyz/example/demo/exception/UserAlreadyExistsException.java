package xyz.example.demo.exception;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException() {
        this("Error: Username is already taken!", "300");
    }

    public UserAlreadyExistsException(String msg, String code) {
        super(msg, code);
    }

    public UserAlreadyExistsException(String msg) {
        super(msg, "300");
    }

}
