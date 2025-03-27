package uz.uat.backend.dto;


import jakarta.validation.constraints.NotBlank;


import java.util.List;


public record ServiceDto(@NotBlank String serviceType,
                         @NotBlank String serviceName_id,
                         @NotBlank String revisionNumber,
                         @NotBlank List<TaskDto> tasks) {
}
