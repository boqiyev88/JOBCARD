package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResultCode(int code, String resultMessage) {
}
