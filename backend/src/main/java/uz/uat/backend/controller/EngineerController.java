package uz.uat.backend.controller;

import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import uz.uat.backend.dto.ServiceDto;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.model.*;
import uz.uat.backend.service.EngineerService;

import java.time.LocalDate;
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
            path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "CSV faylni yuklab, uni qayta ishlaydi va list ko‘rinishida qaytaradi.")
    public ResponseEntity<?> uploadFile(@NonNull @RequestParam("file")
                                        @Parameter(description = "CSV file") MultipartFile file) {
        List<Task> tasks = engineerService.uploadCSV(file);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/generateCSV")
    public ResponseEntity<?> generateCSV(@RequestParam String fileName) {
        Resource resource = engineerService.generateCsvFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tasks.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    @GetMapping("/searchByDate")
    public ResponseEntity<?> searchByDate(@NonNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate,
                                          @NonNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate) {
        List<Work> works = engineerService.searchByDate(startDate, endDate);
        return ResponseEntity.ok(works);
    }

    @PostMapping("/addServices")
    public ResponseEntity<?> addNewService(@NonNull @RequestBody WorkListDto workListDto) {
        engineerService.addNewService(workListDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/mainManu")
    public ResponseEntity<?> mainManu() {
        List<ServiceDto> list = engineerService.getMainManu();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
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
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody TaskDto taskDto) {
        engineerService.editTask(id, taskDto);
        return null;
    }


}
