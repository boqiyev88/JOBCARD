package uz.uat.backend.dto;


import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;
import java.util.List;


public record WorkListDto(@NotBlank String serviceType,
                          @NotBlank String serviceName_id,
                          @NotBlank String revisionNumber,
                          @NotBlank LocalDate revisionTime,
                          @NotBlank List<TaskDto> tasks) {
}
