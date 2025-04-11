package uz.uat.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.service.WorkerService;

@RestController
@RequestMapping("/api/worker")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmployeeController {

    private final WorkerService workerService;

    @GetMapping
    public ResponseEntity<Object> showTask(@RequestParam(value = "status", required = false, defaultValue = "0") int status,
                                           @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        workerService.showTasks(status,page);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("hali tayyormas");

    }
}
