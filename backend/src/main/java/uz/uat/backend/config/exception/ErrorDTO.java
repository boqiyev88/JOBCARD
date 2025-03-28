package uz.uat.backend.config.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDTO {
    private String requestId;
    private String path;
    private String method;
    private String status;
    private String message;
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
}
