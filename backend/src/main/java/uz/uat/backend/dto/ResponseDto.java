package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponseDto(int page,Long total, Object data) {
}
