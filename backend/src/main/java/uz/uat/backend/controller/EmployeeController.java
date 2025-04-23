package uz.uat.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.RespJob;
import uz.uat.backend.service.WorkerService;

@RestController
@RequestMapping("/api/worker")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmployeeController {

    private final WorkerService workerService;

    @GetMapping("/tasks")
    public ResponseEntity<Object> showTasks(@RequestParam(value = "status", required = false, defaultValue = "0") int status) {
        RespJob tasks = workerService.showTasks(status);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tasks);

    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Object> getJob(@PathVariable String jobId) {
        RespJob result = workerService.getById(jobId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<Object> getService(@PathVariable String serviceId) {
        workerService.getService(serviceId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("result");
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> login(@RequestBody LoginController.LoginRequest loginRequest) {
        workerService.login(loginRequest);
        return null;
    }
}
