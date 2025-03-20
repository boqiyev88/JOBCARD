package uz.uat.backend.mapper;

import org.mapstruct.Mapper;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.model.history_models.History;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    History toSpecialist_History(HistoryDto specialistHDto);

}
