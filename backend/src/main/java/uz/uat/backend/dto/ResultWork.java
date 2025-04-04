package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResultWork(String workid, Object service) {
}
