package uz.uat.backend.service.serviceIMPL;


import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.PdfFile;


import java.util.List;

public interface SpecialistServiceIM {

    ResponseJobCardDto addJobCard(JobCardDto jobCardDto);

    PdfFile getPdfFromJob(String jobId);

    ResponseDto changeStatus(RequestStatusDto requestStatusDto, int page);

    ResponseDto returned(RequestDto requestDto);

    ResponseDto getByStatusNum(int status, int page,String search);

    ResponseDto addFileToJob(String jobId, MultipartFile file);

    ResponseDto delete(String jobId);
}
