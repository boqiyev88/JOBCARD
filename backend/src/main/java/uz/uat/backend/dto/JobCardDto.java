package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


import java.time.Instant;

@Builder
public record JobCardDto(
        @NotBlank String workOrder,
        @NotBlank String reg,
        @NotBlank String serialNumber1,
        @NotBlank String engine_1,
        @NotBlank String serialNumber2,
        @NotBlank String engine_2,
        @NotBlank String serialNumber3,
        @NotBlank String apu,
        @NotBlank String serialNumber4,
        @NotBlank String beforelight,
        @NotBlank String fh,
        @NotNull String leg,
        @NotNull String to) {
}
