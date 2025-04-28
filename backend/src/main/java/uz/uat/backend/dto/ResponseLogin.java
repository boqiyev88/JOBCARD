package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponseLogin(ResultCode resultCode,Object token,Object User) {
}
