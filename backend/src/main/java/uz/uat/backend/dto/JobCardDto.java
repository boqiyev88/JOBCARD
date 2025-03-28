package uz.uat.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record JobCardDto(
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
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(type = "string", pattern = "dd-MM-yyyy", example = "28-03-2025")
        LocalDate date,
        @NotNull String leg,
        @NotNull String to) {
}
