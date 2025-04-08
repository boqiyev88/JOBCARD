package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record RequestWorkDto(
        @NotBlank String service_id,
        @NotBlank String threshold,
        @NotBlank String repeat_int,
        @NotBlank String zone,
        @NotBlank String mpr,
        @NotBlank String access,
        @NotBlank String airplane_app,
        @NotBlank String description,
        @NotBlank String access_note,
        @NotBlank String task_description,
        @NotBlank boolean dit,
        @NotBlank boolean avionic,
        @NotBlank boolean mechanic,
        @NotBlank boolean cab_mechanic,
        @NotBlank boolean sheet_metal,
        @NotBlank boolean ndt

) {
}
