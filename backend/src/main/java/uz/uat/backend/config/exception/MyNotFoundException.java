package uz.uat.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyNotFoundException extends MyException {
    public MyNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
