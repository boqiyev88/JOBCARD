package uz.uat.backend.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MyException extends RuntimeException {

    private final HttpStatus status;

    public MyException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
