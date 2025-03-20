package uz.uat.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
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

import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.model.*;
import uz.uat.backend.service.EngineerService;


import java.time.LocalDate;
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
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "PDF faylni yuklab, uni qayta ishlaydi va list ko‘rinishida qaytaradi.")
    public ResponseEntity<?> addUploadPDFFile(@NonNull @RequestParam("file") @Parameter(description = "PDF file") MultipartFile file) {
        List<TaskDto> tasks = engineerService.uploadfile(file);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tasks);
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


    @GetMapping("/search")
    public ResponseEntity<?> searchByDate(@Valid @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                          @Valid @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ResponseServiceDto> services = engineerService.searchByDate(startDate, endDate);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(services);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addNewService(@Valid @RequestBody WorkListDto workListDto) {
        List<ResponseServiceDto> response = engineerService.addNewService(workListDto);
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<?> mainManu() {
        List<ResponseServiceDto> list = engineerService.getMainManu();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable String id) {
        engineerService.getDeleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getServiceName")
    public ResponseEntity<?> getServiceName() {
        List<ServiceName> nameList = engineerService.getServiceName();
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(nameList);
    }

    @GetMapping("/getServiceType")
    public ResponseEntity<?> getServiceType() {
        List<ServiceType> nameList = engineerService.getServiceType();
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(nameList);
    }

    /// hali tayyor emas
    @PutMapping("/put/{id}")
    public ResponseEntity<?> put(@Valid @PathVariable String id, @Valid @RequestBody TaskDto taskDto) {
        engineerService.editTask(id, taskDto);
        return null;
    }


}
