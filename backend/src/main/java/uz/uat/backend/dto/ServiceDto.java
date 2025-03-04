package uz.uat.backend.dto;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ServiceDto(String SERVICETYPE, String SERVICENAME, String REVISONNUMBER, LocalDateTime REVISONTIME) {
}
