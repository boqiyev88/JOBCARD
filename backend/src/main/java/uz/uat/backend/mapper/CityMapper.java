package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.CityDto;
import uz.uat.backend.model.City;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    List<CityDto> toDto(List<City> cities);
    City toEntity(CityDto dto);
}
