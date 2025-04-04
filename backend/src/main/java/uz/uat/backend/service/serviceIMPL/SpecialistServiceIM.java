package uz.uat.backend.service.serviceIMPL;


import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.PdfFile;

public interface SpecialistServiceIM {

    ResponseJobCardDto addJobCard(RequestJobCardDto jobCardDto);

    PdfFile getPdfFromJob(String jobId);

    ResponseDto changeStatus(RequestStatusDto requestStatusDto, int page);

    ResponseDto returned(RequestDto requestDto);

    ResponseDto getByStatusNum(int status, int page,String search);

    ResponseDto addFileToJob(String jobId, MultipartFile file);

    ResponseDto delete(String jobId);

    ResponseDto edit(String jobId, @Valid JobCardDto jobCardDto);

    ResponseWork getWork(String workid);
    ResponsesDtos getJobWithAll(String jobid,int page);
}
