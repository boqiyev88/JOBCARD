package uz.uat.backend.service.serviceIMPL;


import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.ResponseServiceDto;
import uz.uat.backend.dto.ServiceDto;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.model.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EngineerServiceIM {
    List<TaskDto> uploadfile(MultipartFile file);

    List<ResponseServiceDto> getMainManu();

    void getDeleteTask(String id);

    void editTask(String id, TaskDto taskDto);

    List<ServiceName> getServiceName();

    List<ResponseServiceDto> search(LocalDate startDate, LocalDate endDate,String search);

    List<ResponseServiceDto> addNewService(WorkListDto workListDto);


}
