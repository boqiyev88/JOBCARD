package uz.uat.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.RequestWorkDto;
import uz.uat.backend.dto.ResponseDto;
import uz.uat.backend.dto.ResponsesDtos;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.service.EngineerService;
import uz.uat.backend.service.JobService;
import uz.uat.backend.service.TechnicianService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/technician")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;
    private final EngineerService engineerService;


    @PostMapping("/{jobId}")
    public ResponseEntity<?> addWorkBySaveStatus(@Valid @PathVariable(name = "jobId") String jobCard_id,
                                                 @RequestBody List<RequestWorkDto> workDtos) {
        ResponsesDtos list = technicianService.addWork(workDtos, jobCard_id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @GetMapping("/service")
    public ResponseEntity<?> getServices(@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
                                         @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to,
                                         @RequestParam(value = "search", required = false) String search,
                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponsesDtos response = engineerService.getMainManu(from, to, search, page);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping
    public ResponseEntity<Object> getTask(@RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(value = "search", required = false) String search) {
        ResponseDto list = technicianService.getByStatusNum(status, page, search);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @GetMapping("/pdf/{jobId}")
    public ResponseEntity<?> getPDF(@PathVariable String jobId) {
        PdfFile pdfFile = technicianService.getPdfFromJob(jobId);

        if (pdfFile.getData() != null && pdfFile.getId() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfFile.getFileName() + "\"")
                    .body(pdfFile.getData());
        } else
            return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body("file not found");
    }


}
