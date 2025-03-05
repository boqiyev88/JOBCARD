package uz.uat.backend.controller;

import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.ResponseServiceDto;
import uz.uat.backend.dto.ServiceDto;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.model.*;
import uz.uat.backend.service.EngineerService;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/engineer")
@CrossOrigin("*")
public class EngineerController {

    @Autowired
    private final EngineerService engineerService;

    public EngineerController(EngineerService engineerService) {
        this.engineerService = engineerService;
    }

    @PostMapping(
            path = "/uploadCSV",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "CSV faylni yuklab, uni qayta ishlaydi va list ko‘rinishida qaytaradi.")
    public ResponseEntity<?> uploadCSVFile(@NonNull @RequestParam("file")
                                        @Parameter(description = "CSV file") MultipartFile file) {
        List<TaskDto> tasks = engineerService.uploadCSV(file);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping(
            path = "/uploadPDF",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "PDF faylni yuklab, uni qayta ishlaydi va list ko‘rinishida qaytaradi.")
    public ResponseEntity<?> uploadPDFFile(@NonNull @RequestParam("file")
                                        @Parameter(description = "PDF file") MultipartFile file) {
        List<TaskDto> tasks = engineerService.uploadPDF(file);
        return ResponseEntity.ok(tasks);
    }

    /// Faqat CSV file generatsiya qilish uchun
    @GetMapping("/generateCSV")
    public ResponseEntity<?> generateCSV(@Valid @RequestParam String fileName) {
        Resource resource = engineerService.generateCsvFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }


    @GetMapping("/searchByDate")
    public ResponseEntity<?> searchByDate(@Valid @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
                                          @Valid @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        List<ResponseServiceDto> services = engineerService.searchByDate(startDate, endDate);
        return ResponseEntity.ok(services);
    }

    @PostMapping("/addServices")
    public ResponseEntity<?> addNewService(@Valid @RequestBody WorkListDto workListDto) {
        engineerService.addNewService(workListDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/mainManu")
    public ResponseEntity<?> mainManu() {
        List<ServiceDto> list = engineerService.getMainManu();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable String id) {
        engineerService.getDeleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getServiceName")
    public ResponseEntity<?> getServiceName() {
        List<ServiceName> nameList = engineerService.getServiceName();
        return ResponseEntity.status(200).body(nameList);
    }

    @GetMapping("/getServiceType")
    public ResponseEntity<?> getServiceType() {
        List<ServiceType> nameList = engineerService.getServiceType();
        return ResponseEntity.status(200).body(nameList);
    }

    /// hali tayyor emas
    @PutMapping("/put/{id}")
    public ResponseEntity<?> put(@Valid @PathVariable String id, @Valid @RequestBody TaskDto taskDto) {
        engineerService.editTask(id, taskDto);
        return null;
    }


}
