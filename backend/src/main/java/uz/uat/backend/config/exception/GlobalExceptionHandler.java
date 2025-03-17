package uz.uat.backend.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;




import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MyNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(MyNotFoundException e, HttpServletRequest req) {
        return ResponseEntity.status(404)
                .body(ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(404)
                        .errorBody(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MyConflictException.class)
    public ResponseEntity<ErrorDTO> handleConflictException(MyConflictException e, HttpServletRequest req) {
        return ResponseEntity.status(409)
                .body(ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(409)
                        .errorBody(e.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, List<String>> errorMessage = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            errorMessage.compute(field, (s, strings) -> {
                strings = Objects.requireNonNullElse(strings, new ArrayList<>());
                strings.add(defaultMessage);
                return strings;
            });
        }
        return ResponseEntity.status(400)
                .body(ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(400)
                        .errorBody(errorMessage)
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception e, HttpServletRequest req) {
        return ResponseEntity.status(500)
                .body(ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(500)
                        .errorBody("Internal Server Error: " + e.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest req) {
        return ResponseEntity.status(403)
                .body(ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(403)
                        .errorBody("Sizda ushbu resursga ruxsat yo'q!")
                        .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationException(AuthenticationException e, HttpServletRequest req) {
        return ResponseEntity.status(401)
                .body(ErrorDTO.builder()
                        .errorPath(req.getRequestURI())
                        .errorCode(401)
                        .errorBody("Avtorizatsiya talab qilinadi! JWT token noto‘g‘ri yoki muddati tugagan!")
                        .build());
    }
}
