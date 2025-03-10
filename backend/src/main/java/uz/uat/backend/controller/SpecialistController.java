package uz.uat.backend.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestDto;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.model.Specialist_JobCard;
import uz.uat.backend.service.SpecialistService;

@RestController
@RequestMapping("/api/specialist")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SpecialistController {

    private final SpecialistService specialistService;


    @PostMapping(
            path = "/jobCard",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addJobCard(@RequestPart("jobCardDto") @Valid JobCardDto jobCardDto,
                                        @RequestParam("file") @Parameter(description = "PDF file") MultipartFile file) {
        specialistService.addJobCard(jobCardDto, file);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("getId/{id}")
    public ResponseEntity<?> getSpecialistById(@PathVariable("id") String id) {
        Specialist_JobCard service = specialistService.getById(id);
        return ResponseEntity.ok(service);
    }

    @PostMapping("/inprocess/{jobId}")
    public ResponseEntity<?> inProcessSpecialist(@PathVariable String jobId) {
        specialistService.statusInProcess(jobId);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/returned")
    public ResponseEntity<?> returned(@Valid @RequestBody RequestDto requestDto) {
        ///  hali tayyormas
        specialistService.returned(requestDto);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/completed/{jobId}")
    public ResponseEntity<?> confirmSpecialist(@Valid @PathVariable String jobId) {
         specialistService.confirmed(jobId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getPDF/{jobId}")
    public ResponseEntity<?> getPDF(@PathVariable String jobId) {
        PdfFile pdfFile = specialistService.getPdfFromJob(jobId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.getFileName() + "\"")
                .body(pdfFile.getData());
    }


}
