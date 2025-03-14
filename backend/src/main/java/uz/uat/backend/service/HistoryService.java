package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.mapper.HistoryMapper;
import uz.uat.backend.model.history_models.Specialist_History;
import uz.uat.backend.repository.Specialist_HistoryRepository;
import uz.uat.backend.service.serviceIMPL.HistoryServiceIM;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryServiceIM {

    private final HistoryMapper historyMapper;
    private final Specialist_HistoryRepository specialist_historyRepository;

    @Override
    public void addHistory(HistoryDto specialistHDto) {
        Specialist_History specialistHistory = historyMapper.toSpecialist_History(specialistHDto);
        specialist_historyRepository.save(specialistHistory);
    }
}
