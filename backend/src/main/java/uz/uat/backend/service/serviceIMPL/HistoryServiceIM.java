package uz.uat.backend.service.serviceIMPL;

import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.dto.ResponsesDtos;
import uz.uat.backend.model.history_models.History;

import java.time.LocalDate;
import java.util.List;

public interface HistoryServiceIM {

    void addHistory(HistoryDto specialistHDto);

    ResponsesDtos getHistory(LocalDate from, LocalDate to, String search, int page);

}
