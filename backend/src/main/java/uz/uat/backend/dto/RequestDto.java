package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record RequestDto(@NotBlank String jobid, @NotBlank String message) {
}
