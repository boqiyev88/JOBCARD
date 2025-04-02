package uz.uat.backend.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record HistoryDto(
        String tablename,
        String tableID,
        String OS,
        String rowName,
        String oldValue,
        String newValue,
        String updatedBy,
        Instant updTime) {

}
