package uz.uat.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.WorkDto;
import uz.uat.backend.service.TechnicianService;

@RestController
@RequestMapping("/api/technician")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    @PostMapping("/addWork")
    public ResponseEntity<?> addWork(@NonNull @RequestBody WorkDto workDto){
        technicianService.addWork(workDto);
        return null;
    }

    @PostMapping("/closedWork")
    public ResponseEntity<?> closedWork(@NonNull @RequestBody WorkDto workDto){
        technicianService.closedWork(workDto);
        return null;
    }


}
