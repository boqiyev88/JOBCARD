package uz.uat.backend.dto;

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
        LocalDate revisionTime
) {
}
