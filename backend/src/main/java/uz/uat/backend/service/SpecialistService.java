package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.model.JobOverView;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.repository.JobCardRepository;
import uz.uat.backend.service.serviceIMPL.EngineerServiceIM;
import uz.uat.backend.service.serviceIMPL.SpecialistServiceIM;

@Service
@RequiredArgsConstructor
public class SpecialistService implements SpecialistServiceIM {

    private final JobCardRepository jobCardRepository;

    @Override
    public void addJobCard(JobCardDto jobCardDto) {


    }

    @Override
    public void addPdfToJobCard(String jobCardId, MultipartFile file) {
        try {
            String name = file.getName();
            byte[] bytes = file.getBytes();
            jobCardRepository.save(
                    JobOverView.builder()
                            .id(jobCardId)
                            .mainPlan(PdfFile.builder()
                                    .fileName(name)
                                    .data(bytes)
                                    .build())
                            .build()
            );
        } catch (Exception e) {
            throw new MultipartException("INTERNAL SERVER ERROR");
        }

    }
}
