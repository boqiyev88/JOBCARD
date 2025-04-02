package uz.uat.backend.service.serviceIMPL;


import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.*;

import java.time.LocalDate;
import java.util.List;

public interface EngineerServiceIM {
    List<TaskDto> uploadfile(MultipartFile file);

    ResponsesDtos getMainManu(LocalDate from, LocalDate to, String search, int page);

    ResponsesDtos getDeleteTask(String id);

    ResponsesDtos editTask(String id, ServiceDto taskDto);

    List<ServiceNameDto> getServiceName();


    ResponsesDtos addNewService(ServiceDto workListDto);


    ResponsesDtos getServices(String serviceId);
}
