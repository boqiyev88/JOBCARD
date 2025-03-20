package uz.uat.backend.service.serviceIMPL;

import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.model.history_models.History;

import java.util.List;

public interface HistoryServiceIM {

    void addHistory(HistoryDto specialistHDto);

    List<History> getHistory();

}
