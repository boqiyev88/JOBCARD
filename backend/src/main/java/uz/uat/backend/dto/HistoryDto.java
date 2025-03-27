package uz.uat.backend.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record HistoryDto(
        String tablename,
        String tableID,
        String description,
        String rowName,
        String oldValue,
        String newValue,
        String updatedBy,
        Instant updTime) {

}
