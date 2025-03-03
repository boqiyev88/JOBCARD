package uz.uat.backend.service.serviceIMPL;

import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;

public interface SpecialistServiceIM {

     void addJobCard(JobCardDto jobCardDto);

     void addPdfToJobCard(String jobCardId, MultipartFile file);


}
