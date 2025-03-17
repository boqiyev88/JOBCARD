package uz.uat.backend.service.serviceIMPL;

import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.model.history_models.Specialist_History;

import java.util.List;

public interface HistoryServiceIM {

    void addHistory(HistoryDto specialistHDto);

    List<Specialist_History> getHistory();

}
