package uz.uat.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.service.SpecialistService;

@RestController
@RequestMapping("/api/specialist")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SpecialistController {

    private final SpecialistService specialistService;


    @PostMapping("/jobCard")
    public ResponseEntity<?> addJobCard(@Valid @RequestBody JobCardDto jobCardDto) {
        specialistService.addJobCard(jobCardDto);
        return null;
    }

    @PostMapping("/pdf/{id}")
    public ResponseEntity<?> uploadPDF(@PathVariable String id,
                                       @RequestParam("file") MultipartFile file) {
        specialistService.addPdfToJobCard(id, file);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }


}
