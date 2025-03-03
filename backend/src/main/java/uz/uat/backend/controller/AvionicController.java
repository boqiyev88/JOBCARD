package uz.uat.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avionic")
@CrossOrigin("*")
public class AvionicController {


    @GetMapping("/getNewTask")
    public ResponseEntity<?> getNewTask(){

        return null;
    }

    @GetMapping("/getInProcess")
    public ResponseEntity<?> GetAgetInProcessll(){

        return null;
    }
    @GetMapping("/getMessage")
    public ResponseEntity<?> getMessage(){

        return null;
    }
    @GetMapping("/getReference")
    public ResponseEntity<?> getReference(){

        return null;
    }


}
