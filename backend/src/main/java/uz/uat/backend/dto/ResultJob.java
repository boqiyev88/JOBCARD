package uz.uat.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class ResultJob {
    private String work_order;
    private String reg;
    private String serial_number1;
    private String engine_1;
    private String serial_number2;
    private String engine_2;
    private String serial_number3;
    private String apu;
    private String serial_number4;
    private String before_flight;
    private String fh;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(type = "string", pattern = "dd-MM-yyyy", example = "28-03-2025")
    private LocalDate date;
    private String leg;
    private String to;
    private Object work;
    private Object services;
}
