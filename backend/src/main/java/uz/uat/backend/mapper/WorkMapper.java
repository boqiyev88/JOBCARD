package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.RequestWorkDto;
import uz.uat.backend.model.Work;

import java.util.List;


@Mapper(componentModel = "spring")
public interface WorkMapper {

//    List<Work> toFromDto(RequestWorkDto workDto);
}
