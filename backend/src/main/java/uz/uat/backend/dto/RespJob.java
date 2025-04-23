package uz.uat.backend.dto;

import lombok.Builder;


@Builder
public record RespJob(ResultCode resultCode, String resultNode, Object jobs) {
}
