package uz.uat.backend.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.ResponsesDtos;
import uz.uat.backend.service.HistoryService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/history")
@CrossOrigin("*")
public class HistoryController {


    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResponseEntity<Object> getHistory(@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
                                             @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to,
                                             @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponsesDtos history = historyService.getHistory(from, to, page);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(history);
    }


}
