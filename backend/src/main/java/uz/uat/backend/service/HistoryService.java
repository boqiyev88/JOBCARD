package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.mapper.HistoryMapper;
import uz.uat.backend.model.history_models.Specialist_History;
import uz.uat.backend.model.history_models.Technician_History;
import uz.uat.backend.repository.Specialist_HistoryRepository;
import uz.uat.backend.repository.Technician_HistoryRepository;
import uz.uat.backend.service.serviceIMPL.HistoryServiceIM;

@Service
@RequiredArgsConstructor
public class HistoryService implements HistoryServiceIM {
    private final HistoryMapper historyMapper;
    private final Specialist_HistoryRepository specialist_historyRepository;
    private final Technician_HistoryRepository technician_historyRepository;


    @Override
    public void addEngineer() {

    }


    @Override
    public void addSpecialist(HistoryDto specialistHDto) {
        Specialist_History specialistHistory = historyMapper.toSpecialist_History(specialistHDto);
        specialist_historyRepository.save(specialistHistory);
    }

    @Override
    public void addTechnician(HistoryDto historyDto) {
        Technician_History technicianHistory = historyMapper.toTechnician_History(historyDto);
        technician_historyRepository.save(technicianHistory);
    }

    @Override
    public void addWorker() {

    }
}
