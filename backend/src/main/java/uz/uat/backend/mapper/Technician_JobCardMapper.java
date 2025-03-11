package uz.uat.backend.mapper;

import org.mapstruct.Mapper;

import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.model.Technician_JobCard;

@Mapper(componentModel = "spring")
public interface Technician_JobCardMapper {

    Technician_JobCard toEntity(JobCardDto jobCardDto);
    JobCardDto toDto(Technician_JobCard jobCard);
}
