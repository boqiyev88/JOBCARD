package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record JobCardDtoMapper(@NotBlank String WorkOrderNumber,
                               @NotBlank String REG,
                               @NotBlank String SerialNumber1,
                               @NotBlank String ENGINE_1,
                               @NotBlank String SerialNumber2,
                               @NotBlank String ENGINE_2,
                               @NotBlank String SerialNumber3,
                               @NotBlank String APU,
                               @NotBlank String SerialNumber4,
                               @NotBlank String BEFORELIGHT,
                               @NotBlank String FH,
                               @NotNull Instant DATE) {
}
