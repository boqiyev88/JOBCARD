package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.model.history_models.Specialist_History;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    Specialist_History toSpecialist_History(HistoryDto specialistHDto);

}
