package uz.uat.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.*;

import uz.uat.backend.service.EngineerService;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/engineer")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EngineerController {

    private final EngineerService engineerService;

    @PostMapping(
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "PDF faylni yuklab, uni qayta ishlaydi va list ko‘rinishida qaytaradi.")
    public ResponseEntity<?> addUploadPDFFile(@RequestParam("file") @Parameter(description = "PDF file") MultipartFile file) {
        List<TaskDto> tasks = engineerService.uploadfile(file);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tasks);
    }

    /// Faqat CSV file generatsiya qilish uchun
    @GetMapping("/generateCSV")
    public ResponseEntity<?> generateCSV(@Valid @RequestParam String fileName) {
        Resource resource = engineerService.generateCsvFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=service.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }


    @PostMapping
    public ResponseEntity<?> addNewService(@Valid @RequestBody ServiceDto workListDto) {
        ResponsesDtos response = engineerService.addNewService(workListDto);
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") String service_id) {
        ResponsesDtos tasks = engineerService.getServices(service_id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tasks.data());
    }

    @GetMapping
    public ResponseEntity<?> mainManu(@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
                                      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to,
                                      @RequestParam(value = "search", required = false) String search,
                                      @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponsesDtos response = engineerService.getMainManu(from, to, search, page);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable String id) {
        ResponsesDtos response = engineerService.getDeleteTask(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/getServiceName")
    public ResponseEntity<?> getServiceName() {
        List<ServiceNameDto> nameList = engineerService.getServiceName();
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(nameList);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> put(@Valid @PathVariable String id, @Valid @RequestBody ServiceDto workListDto) {
        ResponsesDtos response = engineerService.editTask(id, workListDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }


}
