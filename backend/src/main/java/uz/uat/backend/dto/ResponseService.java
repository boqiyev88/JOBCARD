package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponseService(Object service, Object tasks) {
}
