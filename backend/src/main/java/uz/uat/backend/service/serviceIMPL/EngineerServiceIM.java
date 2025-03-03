package uz.uat.backend.service.serviceIMPL;


import org.springframework.web.multipart.MultipartFile;

public interface EngineerServiceIM {
     void uploadCSV(MultipartFile file);
     void addTaskToWork();
}
