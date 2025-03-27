package uz.uat.backend.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MyNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(MyNotFoundException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MyConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflictException(MyConflictException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, List<String>> errorMessage = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            errorMessage.computeIfAbsent(fieldError.getField(), k -> new ArrayList<>()).add(fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(HttpStatus.BAD_REQUEST.value())
                        .errorBody(errorMessage)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error: " + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.FORBIDDEN, "Sizda ushbu resursga ruxsat yo'q!");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationException(AuthenticationException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.UNAUTHORIZED, "Avtorizatsiya talab qilinadi!  JWT token noto‘g‘ri yoki muddati tugagan!");
    }

    private ResponseEntity<ErrorDTO> buildResponse(HttpServletRequest req, HttpStatus status, Object message) {
        return ResponseEntity.status(status)
                .body(ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorBody(message)
                        .errorCode(status.value())
                        .build());
    }
}
