package uz.uat.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.WorkListDto;

@RestController
@RequestMapping("/api/worklist")
@CrossOrigin("*")
public class WorkListController {


    @PostMapping("/getOne")
    public ResponseEntity<?> getWorkList(@RequestBody WorkListDto workListDto) {

        return ResponseEntity.ok(workListDto);
    }

    @PostMapping("/getAll")
    public ResponseEntity<?> getAllWorkList(@RequestBody WorkListDto workListDto) {

        return ResponseEntity.ok(workListDto);

    }

    @PostMapping("/add")
    public ResponseEntity<?> addWorkList(@RequestBody WorkListDto workListDto) {

        return ResponseEntity.ok(workListDto);

    }


    @PutMapping("/update")
    public ResponseEntity<WorkListDto> updateWorkList(@RequestBody WorkListDto workListDto) {

        return ResponseEntity.ok(workListDto);

    }
    @PutMapping("/delete")
    public ResponseEntity<?> deleteWorkList(@RequestBody WorkListDto workListDto) {

        return ResponseEntity.ok(workListDto);

    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody WorkListDto workListDto) {
        return ResponseEntity.ok("asdaf");
    }
}
