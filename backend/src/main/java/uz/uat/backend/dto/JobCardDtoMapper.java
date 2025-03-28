package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record JobCardDtoMapper(@NotBlank String work_order,
                               @NotBlank String reg,
                               @NotBlank String serial_number1,
                               @NotBlank String engine_1,
                               @NotBlank String serial_number2,
                               @NotBlank String engine_2,
                               @NotBlank String serial_number3,
                               @NotBlank String apu,
                               @NotBlank String serial_number4,
                               @NotBlank String before_flight,
                               @NotBlank String fh
) {
}
