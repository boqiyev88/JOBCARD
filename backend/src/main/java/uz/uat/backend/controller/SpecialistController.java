package uz.uat.backend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestDto;
import uz.uat.backend.dto.RequestStatusDto;
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


    @PostMapping("/add")
    public ResponseEntity<?> addJobCard(@RequestBody @Valid JobCardDto jobCardDto) {
        specialistService.addJobCard(jobCardDto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping(
            path = "/pdf/{jobId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addJobCard(@PathVariable("jobId") String jobId,
                                        @RequestPart("file") @Parameter(description = "PDF file") MultipartFile file) {
        specialistService.addFileToJob(jobId, file);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/status")
    public ResponseEntity<?> inChangeStatus(@RequestBody RequestStatusDto statusDto) {
        specialistService.changeStatus(statusDto);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/returned")
    public ResponseEntity<?> returned(@Valid @RequestBody RequestDto requestDto) {
        ///  hali tayyormas
        specialistService.returned(requestDto);
        return ResponseEntity.status(200).build();
    }


    @GetMapping("/pdf/{jobId}")
    public ResponseEntity<?> getPDF(@PathVariable String jobId) {
        PdfFile pdfFile = specialistService.getPdfFromJob(jobId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.getFileName() + "\"")
                .body(pdfFile.getData());
    }


    @GetMapping("/tasks")
    public ResponseEntity<Object> getTask(@RequestParam("status") int status) {
        List<JobCardDto> newJobCard = specialistService.getByStatus(status);
        log.info("newJobCard: " + newJobCard);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(newJobCard);
    }


}
