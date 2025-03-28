package uz.uat.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ResponseServiceDto(
        String id,
        String service_type,
        String service_name,
        String revision_number,
        @JsonFormat(pattern = "dd-MM-yyyy")LocalDate revision_time
) {
}
