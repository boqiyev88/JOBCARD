package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.service.serviceIMPL.EngineerServiceIM;



@Service
@RequiredArgsConstructor
public class EngineerService implements EngineerServiceIM {


    @Override
    public void uploadCSV(MultipartFile file) {

    }

    @Override
    public void addTaskToWork() {

    }
}
