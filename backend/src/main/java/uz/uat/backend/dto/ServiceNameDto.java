package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ServiceNameDto(String id, String name) {
}
