package uz.uat.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RequestJobCardDto(
        @NotBlank(message = "value is required") String work_order,
        @NotBlank(message = "value is required") String reg,
        @NotBlank(message = "value is required") String serial_number1,
        @NotBlank(message = "value is required") String engine_1,
        @NotBlank(message = "value is required") String serial_number2,
        @NotBlank(message = "value is required") String engine_2,
        @NotBlank(message = "value is required") String serial_number3,
        @NotBlank(message = "value is required") String apu,
        @NotBlank(message = "value is required") String serial_number4,
        @NotBlank(message = "value is required") String before_flight,
        @NotBlank(message = "value is required") String fh,
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(type = "string", pattern = "dd-MM-yyyy", example = "28-03-2025")
        LocalDate date,
        @NotNull(message = "value is required") String leg,
        @NotNull(message = "value is required") String to) {
}
