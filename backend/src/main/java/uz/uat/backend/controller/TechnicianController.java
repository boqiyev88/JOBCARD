package uz.uat.backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.service.EngineerService;
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


//    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    @PostMapping("/{jobId}")
    public ResponseEntity<?> addWorkBySaveStatus(@Valid @PathVariable(name = "jobId") String jobCard_id,
                                                 @RequestBody List<RequestWorkDto> workDtos) {
        ResponsesDtos list = technicianService.addWork(workDtos, jobCard_id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

//    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    @GetMapping("/service")
    public ResponseEntity<?> getServices(@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
                                         @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to,
                                         @RequestParam(value = "search", required = false) String search,
                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponsesDtos response = engineerService.getMainManu(from, to, search, page);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

//    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    @GetMapping
    public ResponseEntity<Object> getTask(@RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(value = "search", required = false) String search) {
        ResponseDto list = technicianService.getByStatusNum(status, page, search);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

//    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    @GetMapping("/work/{jobid}")
    ResponseEntity<?> getWorkByJobId(@PathVariable(name = "jobid") String jobCard_id) {
        ResponseWork responseWork = technicianService.showWorksWithService(jobCard_id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseWork);
    }

//    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    @PutMapping("/{jobid}")
    public ResponseEntity<?> updateWorkById(@PathVariable(name = "jobid") String jobid, @RequestBody List<RequestEditWork> workDto) {
        ResponseDto responseDto = technicianService.edit(jobid, workDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseDto);
    }

//    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
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

//    @PreAuthorize("hasRole('ROLE_TECHNICIAN')")
    @DeleteMapping("/{workid}")
    public ResponseEntity<?> deleteWorkById(@PathVariable(name = "workid") String workid, @RequestBody DeleteWorkDto deleteWorkDto) {

        ResponseWork response = technicianService.delete(workid, deleteWorkDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


}
