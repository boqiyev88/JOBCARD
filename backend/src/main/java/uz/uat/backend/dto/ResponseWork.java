package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponseWork(Object job, Object work, Object tasks) {
}
