package uz.uat.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.service.EngineerService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/engineer")
@CrossOrigin("*")
public class EngineerController {

    @Autowired
    private final EngineerService engineerService;

    public EngineerController(EngineerService engineerService) {
        this.engineerService = engineerService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@NonNull @RequestParam("file") MultipartFile file) {

        return ResponseEntity.badRequest().body("Faqat CSV formatidagi fayllarni yuklang!");
    }

    @GetMapping("/searchByDate")
    public ResponseEntity<WorkListDto> searchByDate(@NonNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @NonNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return null;
    }


    @PostMapping("/addTask")
    public ResponseEntity<?> addTaskToWork(@NonNull @RequestBody WorkListDto workListDto) {

        return null;
    }



}
