package uz.uat.backend.service.serviceIMPL;


import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.PdfFile;

import java.util.List;

public interface SpecialistServiceIM {

    ResponseJobCardDto addJobCard(RequestJobCardDto jobCardDto);

    PdfFile getPdfFromJob(String jobId);

    ResponseDto changeStatus(RequestStatusDto requestStatusDto);

    ResponseDto returned(RequestDto requestDto);

    ResponseDto getByStatusNum(int status, int page,String search);

    ResponseDto addFileToJob(String jobId, MultipartFile file);

    ResponseDto delete(String jobId);

    ResponseDto edit(String jobId, @Valid JobCardDto jobCardDto);

    ResultJob getWork(String workid);
    ResultJob getJobWithAll(String jobid);

    List<ResultWork> getWorks(String jobid);

    ResponseJobCardDto getJob(String jobId);
}
