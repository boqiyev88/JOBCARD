package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.JobCard;


@Mapper(componentModel = "spring")
public interface JobCardMapper {

    JobCard toEntity(JobCardDtoMapper jobCardDto);

    JobCardDtoMapper toDtoMapper(RequestJobCardDto jobCardDto);
    ResultJob fromDto(ResponseJobCardDto jobCardDto);
}
