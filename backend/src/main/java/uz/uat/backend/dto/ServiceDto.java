package uz.uat.backend.dto;

import lombok.Builder;
import uz.uat.backend.model.ServiceName;
import uz.uat.backend.model.ServiceType;



import java.time.LocalDate;


@Builder
public record ServiceDto(
        ServiceName serviceName,
        ServiceType serviceType,
        String revisonNumber,
        LocalDate revisonTime) {
}
