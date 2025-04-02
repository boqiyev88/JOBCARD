package uz.uat.backend.dto;

import lombok.Builder;


@Builder
public record ResponseDto(int page, Long total,Long all, Long New, Long Pending, Long In_process, Long Confirmed, Long Completed, Long Rejected, Object data) {
}
