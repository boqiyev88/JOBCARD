package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponseWork(Object jobcard, Object work) {
}
