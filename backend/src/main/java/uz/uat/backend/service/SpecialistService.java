package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestDto;
import uz.uat.backend.mapper.JobCardMapper;
import uz.uat.backend.mapper.Specialist_JobCardMapper;
import uz.uat.backend.mapper.Technician_JobCardMapper;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.model.Specialist_JobCard;
import uz.uat.backend.model.Technician_JobCard;
import uz.uat.backend.model.enums.SpecialistStatus;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.repository.JobCardRepository;
import uz.uat.backend.repository.PDFfileRepository;
import uz.uat.backend.repository.Specialist_JobCardRepository;
import uz.uat.backend.repository.Technician_JobCardRepository;
import uz.uat.backend.service.serviceIMPL.SpecialistServiceIM;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SpecialistService implements SpecialistServiceIM {

    private final JobCardMapper jobCardMapper;
    private final JobCardRepository jobCardRepository;
    private final PDFfileRepository pdffileRepository;
    private final Technician_JobCardMapper technicianJobCardMapper;
    private final Specialist_JobCardMapper specialistJobCardMapper;
    private final Technician_JobCardRepository technicianJobCardRepository;
    private final Specialist_JobCardRepository specialistJobCardRepository;

    @Override
    public void addJobCard(JobCardDto jobCardDto, MultipartFile file) {
        if (jobCardDto == null || file == null || file.isEmpty()) {
            throw new MultipartException("Invalid data file or input object");
        }
        try {
            JobCard jobCard = jobCardMapper.toEntity(jobCardDto);
            PdfFile file1 = pdffileRepository.save(PdfFile.builder()
                    .fileName(file.getName())
                    .data(file.getBytes())
                    .build());
            jobCard.setMainPlan(file1);
            jobCardRepository.save(jobCard);

            Specialist_JobCard specialist_jobCard = specialistJobCardMapper.toEntity(jobCardDto);
            specialist_jobCard.setMainPlan(PdfFile.builder()
                    .fileName(file.getName())
                    .data(file.getBytes())
                    .build());
            specialist_jobCard.setSTATUS(SpecialistStatus.NEW);
            specialistJobCardRepository.save(specialist_jobCard);

            Technician_JobCard technician_jobCard = technicianJobCardMapper.toEntity(jobCardDto);
            technician_jobCard.setSTATUS(Status.NEW);
            technician_jobCard.setMainPlan(PdfFile.builder()
                    .fileName(file.getName())
                    .data(file.getBytes())
                    .build());
            technicianJobCardRepository.save(technician_jobCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPdfToJobCard(String jobCardId, MultipartFile file) {
        try {
            String name = file.getName();
            byte[] bytes = file.getBytes();
            jobCardRepository.save(
                    JobCard.builder()
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

    @Override
    public void returned(RequestDto requestDto) {
        if (requestDto.id() == null || requestDto.massage() == null)
            throw new UsernameNotFoundException("invalid request id or massage is null");

        try {
            Specialist_JobCard jobCardId = getById(requestDto.id());
            Technician_JobCard tj = getTechnician(jobCardId.getWorkOrderNumber());

            ///  shu joyda Texnikka massage jo'natilishi kerak
            tj.setSTATUS(Status.REJECTED);
            technicianJobCardRepository.save(tj);

            jobCardId.setSTATUS(SpecialistStatus.REJECTED);
            specialistJobCardRepository.save(jobCardId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void completedTask(String jobId) {
        ///  real timeda status o'zgartirish kerak
        try {
            Specialist_JobCard jobCard = getById(jobId);
            jobCard.setSTATUS(SpecialistStatus.COMPLETED);
            specialistJobCardRepository.save(jobCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void statusInProcess(String jobId) {
        ///  texnikda ham inprocess status yoqilishi kerak undan oldin job card va work bir biriga qanday bog'langanini bilish kerak
        try {

            Specialist_JobCard jobCardId = specialistJobCardRepository.findByJobCardId(jobId);
            if (jobCardId == null)
                throw new UsernameNotFoundException("job card not found, may be Invalid job card id");

            Optional<Technician_JobCard> optional =
                    technicianJobCardRepository.findByWorkOrderNumber(jobCardId.getWorkOrderNumber());

            if (optional.isEmpty() || optional.get() == null)
                throw new UsernameNotFoundException("technician job card not found, may be Invalid job card id");
            Technician_JobCard tj = optional.get();

            ///  shu joyda websocket ishlatish kerak real time bilan ishlash uchun
            tj.setSTATUS(Status.IN_PROCESS);
            technicianJobCardRepository.save(tj);

            jobCardId.setSTATUS(SpecialistStatus.IN_PROCESS);
            specialistJobCardRepository.save(jobCardId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PdfFile getPdfFromJob(String jobId) {
        Specialist_JobCard jobCardId = specialistJobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null)
            throw new UsernameNotFoundException("job card not found");
        return jobCardId.getMainPlan();
    }

    @Override
    public Specialist_JobCard getById(String id) {
        Specialist_JobCard jobCardId = specialistJobCardRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new UsernameNotFoundException("job card not found, may be Invalid job card id");

        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
    }

    @Override
    public Technician_JobCard getTechnician(String workOrderNumber) {

        Optional<Technician_JobCard> optional =
                technicianJobCardRepository.findByWorkOrderNumber(workOrderNumber);
        if (optional.isEmpty())
            throw new UsernameNotFoundException("technician job card not found, may be Invalid job card id");

        return optional.get();
    }
}
