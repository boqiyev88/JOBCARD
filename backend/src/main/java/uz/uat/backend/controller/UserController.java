package uz.uat.backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // Frontend porti
public class UserController {

    @GetMapping("/hello")
    public Map<String, String> hello(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        System.out.println("✅ KELGAN Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠ Authorization header yo‘q yoki noto‘g‘ri formatda!");
            return Map.of("error", "⚠ Authorization header noto‘g‘ri yoki yo‘q");
        }

        return Map.of("message", "Hello from Spring Boot!", "token", authHeader);
    }
}