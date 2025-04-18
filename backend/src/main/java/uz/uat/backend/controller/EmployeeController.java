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
    public ResponseEntity<Object> showTask(@RequestParam(value = "status", required = false, defaultValue = "0") int status) {
        workerService.showTasks(status);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("hali tayyormas");

    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> login(@RequestBody LoginController.LoginRequest loginRequest) {
        workerService.login(loginRequest);
        return null;
    }
}
