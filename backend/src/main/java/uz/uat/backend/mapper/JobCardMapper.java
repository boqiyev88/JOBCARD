package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.model.JobCard;

@Mapper(componentModel = "spring")
public interface JobCardMapper {

    JobCardDto toDto(JobCard jobCard);

    JobCard toEntity(JobCardDto jobCardDto);
}
