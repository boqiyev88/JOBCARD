package uz.uat.backend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.service.SpecialistService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/specialist")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SpecialistController {

    private final SpecialistService specialistService;


    @PostMapping
    public ResponseEntity<?> addJobCard(@RequestBody @Valid RequestJobCardDto jobCardDto) {
        ResponseJobCardDto resp = specialistService.addJobCard(jobCardDto);
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(resp);
    }

    @PostMapping(
            path = "/pdf/{jobId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addJobCard(@PathVariable("jobId") String jobId,
                                        @RequestPart("file") @Parameter(description = "PDF file") MultipartFile file) {
        ResponseDto job = specialistService.addFileToJob(jobId, file);
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(job);
    }

    @PostMapping("/status")
    public ResponseEntity<?> inChangeStatus(@RequestBody RequestStatusDto statusDto) {
        ResponseDto dtos = specialistService.changeStatus(statusDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dtos);
    }

    @PostMapping("/returned")
    public ResponseEntity<?> returned(@Valid @RequestBody RequestDto requestDto) {
        ResponseDto returned = specialistService.returned(requestDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(returned);
    }


    @GetMapping("/pdf/{jobId}")
    public ResponseEntity<?> getPDF(@PathVariable String jobId) {
        PdfFile pdfFile = specialistService.getPdfFromJob(jobId);

        if (pdfFile.getData() != null && pdfFile.getId() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.getFileName() + "\"")
                    .body(pdfFile.getData());
        } else
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("file not found");
    }

    @GetMapping("/services/{jobid}")
    ResponseEntity<?> getWork(@PathVariable String jobid) {
        List<ResultWork> resultWorks = specialistService.getWorks(jobid);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(resultWorks);
    }


    @GetMapping
    public ResponseEntity<Object> getTask(@RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(value = "search", required = false) String search) {
        ResponseDto list = specialistService.getByStatusNum(status, page, search);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @GetMapping("/work/{workid}")
    public ResponseEntity<?> getWorks(@PathVariable("workid") String workid) {
        ResultJob responseWork = specialistService.getWork(workid);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseWork);
    }

    @GetMapping("/toconfirmation/{jobid}")
    public ResponseEntity<?> getJob(@PathVariable(value = "jobid") String jobid) {
        ResultJob responseWork = specialistService.getJobWithAll(jobid);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseWork);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJobCard(@PathVariable("id") String jobId,
                                           @RequestBody @Valid JobCardDto jobCardDto) {
        ResponseDto responseDto = specialistService.edit(jobId, jobCardDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseDto);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJobCard(@Valid @PathVariable("jobId") String jobId) {
        ResponseDto delete = specialistService.delete(jobId);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(delete);
    }


}
