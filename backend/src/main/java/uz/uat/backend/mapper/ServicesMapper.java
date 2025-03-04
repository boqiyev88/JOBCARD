package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.ServiceDto;
import uz.uat.backend.model.Services;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServicesMapper {
    List<ServiceDto> toDto(List<Services> services);

}
