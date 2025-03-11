package uz.uat.backend.service.serviceIMPL;

import uz.uat.backend.dto.HistoryDto;

public interface HistoryServiceIM {

    void addEngineer();

    void addSpecialist(HistoryDto specialistHDto);

    void addTechnician(HistoryDto historyDto);

    void addWorker();


}
