package uz.uat.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.uat.backend.model.history_models.Specialist_History;
import uz.uat.backend.service.HistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin("*")
public class HistoryController {


    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResponseEntity<?> getHistory() {
        List<Specialist_History> history = historyService.getHistory();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(history);
    }
}
