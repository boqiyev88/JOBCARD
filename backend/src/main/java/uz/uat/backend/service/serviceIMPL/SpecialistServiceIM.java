package uz.uat.backend.service.serviceIMPL;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestDto;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.model.Specialist_JobCard;
import uz.uat.backend.model.Technician_JobCard;
import uz.uat.backend.model.enums.Status;

import java.util.List;

public interface SpecialistServiceIM {

     void addJobCard(JobCardDto jobCardDto, MultipartFile file);

     PdfFile getPdfFromJob(String jobId);

     Specialist_JobCard getById(String id);

     void statusInProcess(String jobId);

     void returned(RequestDto requestDto);

     Technician_JobCard getTechnician(String id);

     void confirmed(@Valid String jobId);

     List<JobCardDto> getByStatus(Status status);

     List<JobCardDto> getAll();
}
