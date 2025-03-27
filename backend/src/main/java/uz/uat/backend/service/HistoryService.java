package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.mapper.HistoryMapper;
import uz.uat.backend.model.history_models.History;
import uz.uat.backend.repository.HistoryRepository;
import uz.uat.backend.service.serviceIMPL.HistoryServiceIM;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryServiceIM {

    private final HistoryMapper historyMapper;
    private final HistoryRepository historyRepository;

    @Override
    public void addHistory(HistoryDto specialistHDto) {
        History specialistHistory = historyMapper.toSpecialist_History(specialistHDto);
        historyRepository.save(specialistHistory);
    }

    @Override
    public List<History> getHistory() {
        List<History> list = historyRepository.findAll();
        if (list.isEmpty())
            throw new MyNotFoundException("anything history found");
        return list;
    }
}
