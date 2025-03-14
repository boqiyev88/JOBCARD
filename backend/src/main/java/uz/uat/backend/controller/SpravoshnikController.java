package uz.uat.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.uat.backend.dto.CityDto;
import uz.uat.backend.service.SpcService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/spc")
@CrossOrigin("*")
public class SpravoshnikController {

    private final SpcService spcService;

    @GetMapping("/city")
    public ResponseEntity<?> getCity() {
        List<CityDto> citys = spcService.getCitys();
        return ResponseEntity.ok(citys);
    }


}
