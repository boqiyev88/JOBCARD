package uz.uat.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import uz.uat.backend.model.Task;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ResponseServiceDto(
        String id,
        String service_type,
        String service_name,
        String revisionNumber,
        @JsonFormat(pattern = "dd-MM-yyyy")LocalDate revisionTime
) {
}
