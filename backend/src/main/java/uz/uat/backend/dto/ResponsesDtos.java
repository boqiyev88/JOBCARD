package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponsesDtos(int page, Long total, Object data) {
}
