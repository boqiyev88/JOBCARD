package uz.uat.backend.dto;

import lombok.Builder;
import uz.uat.backend.model.ServiceName;
import uz.uat.backend.model.ServiceType;
import uz.uat.backend.model.Task;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ResponseServiceDto(String ID, String SERVICETYPE, String SERVICENAME, String REVISONNUMBER, LocalDate REVISONTIME, List<Task> tasks) {
}
