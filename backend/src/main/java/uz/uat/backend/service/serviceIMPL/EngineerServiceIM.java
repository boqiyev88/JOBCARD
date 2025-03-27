package uz.uat.backend.service.serviceIMPL;


import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.*;

import java.time.LocalDate;
import java.util.List;

public interface EngineerServiceIM {
    List<TaskDto> uploadfile(MultipartFile file);

    ResponseDto getMainManu(LocalDate from, LocalDate to, String search, int page);

    ResponseDto getDeleteTask(String id);

    ResponseDto editTask(String id, ServiceDto taskDto);

    List<ServiceNameDto> getServiceName();


    ResponseDto addNewService(ServiceDto workListDto);


}
