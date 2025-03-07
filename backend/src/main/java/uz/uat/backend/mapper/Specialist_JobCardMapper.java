package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.model.Specialist_JobCard;

@Mapper(componentModel = "spring")
public interface Specialist_JobCardMapper {

    Specialist_JobCard toEntity(JobCardDto jobCardDto);

    JobCardDto toDto(Specialist_JobCard specialist_JobCard);
}
