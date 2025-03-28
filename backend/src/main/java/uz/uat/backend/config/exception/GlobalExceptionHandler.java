package uz.uat.backend.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MyNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(MyNotFoundException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.NOT_FOUND, "The requested endpoint does not exist: " + req.getRequestURI());
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorDTO> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.NOT_FOUND, "The requested endpoint does not exist: " + req.getRequestURI());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorDTO> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.NOT_FOUND, "The requested endpoint does not exist: " + req.getRequestURI());
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
                .body(ErrorDTO.builder()
                        .requestId(UUID.randomUUID().toString())
                        .path(req.getRequestURI())
                        .method(req.getMethod())
                        .message(errorMessage.toString())
                        .status(HttpStatus.BAD_REQUEST.name())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.FORBIDDEN, "You do not have permission to access this resource!");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationException(AuthenticationException e, HttpServletRequest req) {
        return buildResponse(req, HttpStatus.UNAUTHORIZED, "Authentication is required! JWT token is invalid or expired.");
    }

    private ResponseEntity<ErrorDTO> buildResponse(HttpServletRequest req, HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ErrorDTO.builder()
                        .requestId(UUID.randomUUID().toString())
                        .path(req.getRequestURI())
                        .method(req.getMethod())
                        .message(message)
                        .status(status.toString())
                        .build());
    }
}
