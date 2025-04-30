package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RequestJob(@NotBlank String jobId, @NotBlank String serviceId) {
}
