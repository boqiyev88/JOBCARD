package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record ToWorkMapper(
        @NotBlank String threshold,
        @NotBlank String repeat_int,
        @NotBlank String zone,
        @NotBlank String mrf,
        @NotBlank String access,
        @NotBlank String airplane_app,
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
