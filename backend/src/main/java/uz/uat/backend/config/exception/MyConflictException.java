package uz.uat.backend.config.exception;

import org.springframework.http.HttpStatus;

public class MyConflictException extends MyException {
    public MyConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
