package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record Result(Object job, Object work, Object service) {
}
