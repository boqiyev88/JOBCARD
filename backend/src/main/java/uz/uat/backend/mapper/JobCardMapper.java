package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestJobCardDto;
import uz.uat.backend.dto.JobCardDtoMapper;
import uz.uat.backend.model.JobCard;


@Mapper(componentModel = "spring")
public interface JobCardMapper {

    JobCard toEntity(JobCardDtoMapper jobCardDto);

    JobCardDtoMapper toDtoMapper(RequestJobCardDto jobCardDto);

    JobCardDtoMapper toJDtoMapper(JobCardDto jobCardDto);
    RequestJobCardDto fromDto(JobCardDto jobCardDto);
}
