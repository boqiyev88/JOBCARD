package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponseMessage(String id, String title, String fromUser, String toUser) {
}
