package uz.uat.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record JobCardDto(
        String work_order,
        String reg,
        String serial_number1,
        String engine_1,
        String serial_number2,
        String engine_2,
        String serial_number3,
        String apu,
        String serial_number4,
        String before_flight,
        String fh,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(type = "string", pattern = "dd-MM-yyyy", example = "28-03-2025")
        LocalDate date,
        String leg,
        String to) {
}
