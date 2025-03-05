package uz.uat.backend.dto;

import lombok.Builder;
import uz.uat.backend.model.Task;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResponseServiceDto(String ID, String SERVICETYPE, String SERVICENAME, String REVISONNUMBER, LocalDateTime REVISONTIME, List<Task> tasks) {
}
