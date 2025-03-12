package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.Specialist_JobCard;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobCardMapper {

    JobCard toEntity(JobCardDto jobCardDto);

    JobCardDto toDto(JobCard JobCard);

    List<JobCardDto> toDto(List<JobCard> JobCards);
}
