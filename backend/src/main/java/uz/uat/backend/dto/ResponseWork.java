package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record ResponseWork(String job_status,Object work) {
}
