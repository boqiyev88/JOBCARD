package uz.uat.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ResponseJobCardDto(
        @NotBlank String work_order,
        @NotBlank String reg,
        @NotBlank String serial_number1,
        @NotBlank String engine_1,
        @NotBlank String serial_number2,
        @NotBlank String engine_2,
        @NotBlank String serial_number3,
        @NotBlank String apu,
        @NotBlank String serial_number4,
        @NotBlank String before_flight,
        @NotBlank String fh,
        @NotNull String leg,
        @NotNull String to,
        @JsonFormat(pattern = "dd-MM-yyyy")
        @NotNull LocalDate date,
        @NotBlank String status
) {
}
