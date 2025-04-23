package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record JobWithService(Object job, Object service) {
}
