package uz.uat.backend.dto;



import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;



public record WorkListDto(@NotBlank String serviceType_id,
                          @NotBlank String serviceName_id,
                          @NotBlank String revisionNumber,
                          @NotBlank LocalDate revisionTime) {
}
