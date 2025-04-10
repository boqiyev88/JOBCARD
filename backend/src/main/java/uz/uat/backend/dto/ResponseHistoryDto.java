package uz.uat.backend.dto;

import lombok.Builder;



@Builder
public record ResponseHistoryDto(
        String tablename,
        String tableID,
        String OS,
        String rowName,
        String oldValue,
        String newValue,
        String updatedBy,
        String updTime
) {
}
