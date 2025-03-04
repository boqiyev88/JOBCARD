package uz.uat.backend.service.serviceIMPL;


import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.ServiceDto;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EngineerServiceIM {
     List<Task>  uploadCSV(MultipartFile file);


     List<ServiceDto> getMainManu();

     void getDeleteTask(String id);

     void editTask(String id, TaskDto taskDto);

     List<ServiceName> getServiceName();

     List<Work> searchByDate(LocalDateTime startDate, LocalDateTime endDate);

     void addNewService(WorkListDto workListDto);

     List<ServiceType> getServiceType();

}
