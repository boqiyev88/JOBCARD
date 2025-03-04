package uz.uat.backend.dto;



import java.time.LocalDateTime;


public record WorkListDto(String serviceType, String serviceName, String revisionNumber, LocalDateTime revisionTime) {
}
