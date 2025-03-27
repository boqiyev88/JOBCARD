package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.ServiceNameDto;
import uz.uat.backend.model.ServiceName;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceNameMapper {
    List<ServiceNameDto> fromEntity(List<ServiceName> list);
}
