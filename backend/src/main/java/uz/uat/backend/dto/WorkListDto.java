package uz.uat.backend.dto;



import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;



public record WorkListDto(@NotBlank String serviceType,
                          @NotBlank String serviceName,
                          @NotBlank String revisionNumber,
                          @NotBlank LocalDate revisionTime) {
}
