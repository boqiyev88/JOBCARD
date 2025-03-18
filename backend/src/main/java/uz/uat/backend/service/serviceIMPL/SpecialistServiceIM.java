package uz.uat.backend.service.serviceIMPL;


import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestDto;
import uz.uat.backend.dto.RequestStatusDto;
import uz.uat.backend.dto.ResponseJobCardDto;
import uz.uat.backend.model.PdfFile;


import java.util.List;

public interface SpecialistServiceIM {

     List<ResponseJobCardDto>  addJobCard(JobCardDto jobCardDto);

     PdfFile getPdfFromJob(String jobId);

     void changeStatus(RequestStatusDto statusDto);

     void returned(RequestDto requestDto);

     List<ResponseJobCardDto> getByStatusNum(int status);

     void addFileToJob(String jobId,MultipartFile file);
}
