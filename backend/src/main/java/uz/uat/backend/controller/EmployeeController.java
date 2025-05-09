package uz.uat.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.LoginResponse;
import uz.uat.backend.dto.RequestJob;
import uz.uat.backend.dto.RespJob;
import uz.uat.backend.dto.ResultService;
import uz.uat.backend.service.WorkerService;

@RestController
@RequestMapping("/api/worker")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmployeeController {

    private final WorkerService workerService;


    //    @PreAuthorize("hasRole('ROLE_WORKER')")
    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> login(@RequestBody LoginController.LoginRequest loginRequest) {
        LoginResponse login = workerService.login(loginRequest);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(login);
    }

    //    @PreAuthorize("hasRole('ROLE_WORKER')")
    @GetMapping("/tasks")
    public ResponseEntity<Object> showTasks(@RequestParam(value = "status", required = false, defaultValue = "0") int status) {
        RespJob tasks = workerService.showTasks(status);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tasks);

    }

    //        @PreAuthorize("hasRole('ROLE_WORKER')")
    @GetMapping("job/{jobId}")
    public ResponseEntity<Object> getJob(@PathVariable String jobId) {
        RespJob result = workerService.getById(jobId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result);
    }

    //        @PreAuthorize("hasRole('ROLE_WORKER')")
    @PostMapping(value = "/getService", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getService(@RequestBody RequestJob requestJob) {
        ResultService respJob = workerService.getService(requestJob);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(respJob);
    }

    //        @PreAuthorize("hasRole('ROLE_WORKER')")
    @PostMapping(value = "/changeStatus", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> changeStatus(@RequestBody RequestJob requestJob) {
        ResultService respJob = workerService.changeWorkStatus(requestJob);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(respJob);
    }
}
