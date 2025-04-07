package uz.uat.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.RequestWorkDto;
import uz.uat.backend.dto.ResponseDto;
import uz.uat.backend.dto.ResponsesDtos;
import uz.uat.backend.service.TechnicianService;

import java.util.List;

@RestController
@RequestMapping("/api/technician")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianService technicianService;

    @PostMapping("/addWork/{jobId}")
    public ResponseEntity<?> addWork(@Valid @PathVariable(name = "jobId") String jobCard_id,
                                     @RequestBody List<RequestWorkDto> workDtos) {
        ResponsesDtos list = technicianService.addWork(workDtos, jobCard_id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }

    @PostMapping("/closedWork")
    public ResponseEntity<?> closedWork(@NonNull @RequestBody RequestWorkDto workDto) {
//        technicianService.closedWork(workDto);
        return null;
    }

    @GetMapping
    public ResponseEntity<Object> getTask(@RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(value = "search", required = false) String search) {
        ResponseDto list = technicianService.getByStatusNum(status, page, search);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(list);
    }





}
