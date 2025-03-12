package uz.uat.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.dto.JobCardDto;
import uz.uat.backend.dto.RequestDto;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.dto.RequestStatusDto;
import uz.uat.backend.mapper.JobCardMapper;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.model.Technician_JobCard;
import uz.uat.backend.model.enums.Status;


import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.Technician_JobCardRepository;
import uz.uat.backend.service.serviceIMPL.SpecialistServiceIM;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SpecialistService implements SpecialistServiceIM {


    private final JobCardMapper jobCardMapper;
    private final Technician_JobCardRepository technicianJobCardRepository;
    private final JobCarRepository specialistJobCardRepository;
    private final Notifier notifier;
    private final HistoryService historyService;

    @Transactional
    @Override
    public void addJobCard(JobCardDto jobCardDto) {
        if (jobCardDto == null) {
            throw new UsernameNotFoundException("jobCardDto is null");
        }
        try {
            JobCard specialist_jobCard = jobCardMapper.toEntity(jobCardDto);
            specialist_jobCard.setSTATUS(Status.NEW);
            JobCard jobCard = specialistJobCardRepository.save(specialist_jobCard);
//            notifier.SpecialistNotifier(saveSpecialist);
//            notifier.SpecialistMassageNotifier("New JobCard added");
            /// historyga yozildi
            historyService.addSpecialist(HistoryDto.builder()
                    .tableID("Job Card table")
                    .description("New Job Card added")
                    .rowName("Status")
                    .oldValue(" ")
                    .newValue(Status.NEW.name())
                    .updatedBy(jobCard.getUpdUser())
                    .updTime(Instant.now())
                    .build());

//            notifier.TechnicianNotifier(saveTechnician);
//            notifier.TechnicianMassageNotifier("New JobCard added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void addFileToJob(String jobId, MultipartFile file) {
        try {
            JobCard jobCard = getById(jobId);
            jobCard.setMainPlan(PdfFile.builder()
                    .fileName(file.getName())
                    .data(file.getBytes())
                    .build()
            );
            specialistJobCardRepository.save(jobCard);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void returned(RequestDto requestDto) {
        if (requestDto.id() == null || requestDto.massage() == null)
            throw new UsernameNotFoundException("invalid request id or massage is null");

        try {
            JobCard jobCardId = getById(requestDto.id());


            jobCardId.setSTATUS(Status.REJECTED);
            JobCard specialist_jobCard =
                    specialistJobCardRepository.updateSTATUSById(Status.REJECTED.name(), jobCardId.getId());

            notifier.SpecialistNotifier(specialist_jobCard);                /// real timeda specialist tablega jo'natildi
            notifier.TechnicianMassageNotifier("Sizda Tasdiqdan o'tmagan ish mavjud");    /// uvidemleniyaga ish reject bo'lgani haqida habar jo'natildi
            historyService.addSpecialist(HistoryDto.builder()
                    .tableID("Job Card table")
                    .description("Job Card status updated")
                    .rowName("Status")
                    .oldValue(jobCardId.getSTATUS().name())
                    .newValue(Status.REJECTED.name())
                    .updatedBy(specialist_jobCard.getUpdUser())
                    .updTime(Instant.now())
                    .build());
            notifier.TechnicianNotifier(specialist_jobCard);               /// real timeda texnik tablega jo'natildi
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Transactional
    @Override
    public void changeStatus(RequestStatusDto statusDto) {
        switch (statusDto.status()) {
            case 1:
                changed(statusDto.jobId(), Status.NEW.name());
            case 2:
                changed(statusDto.jobId(), Status.PENDING.name());
            case 3:
                changed(statusDto.jobId(), Status.IN_PROCESS.name());
            case 4:
                changed(statusDto.jobId(), Status.CONFIRMED.name());
            case 5:
                changed(statusDto.jobId(), Status.COMPLETED.name());
            case 6:
                changed(statusDto.jobId(), Status.REJECTED.name());
        }
    }

    @Override
    public PdfFile getPdfFromJob(String jobId) {
        JobCard jobCardId = specialistJobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null)
            throw new UsernameNotFoundException("job card not found");
        return jobCardId.getMainPlan();
    }


    public List<JobCardDto> getByStatus(int status) {
        switch (status) {
            case 1:
                return getByStatus(Status.NEW.name());
            case 2:
                return getByStatus(Status.PENDING.name());
            case 3:
                return getByStatus(Status.IN_PROCESS.name());
            case 4:
                return getByStatus(Status.CONFIRMED.name());
            case 5:
                return getByStatus(Status.COMPLETED.name());
            case 6:
                return getByStatus(Status.REJECTED.name());
            default:
                return getAll();
        }

    }


    private void changed(String jobId, String status) {
        try {
            JobCard jobCard = getById(jobId);
            JobCard specialist_jobCard = specialistJobCardRepository.updateSTATUSById(status, jobCard.getId());
            notifier.SpecialistNotifier(specialist_jobCard);


            notifier.TechnicianNotifier(jobCard);
            notifier.TechnicianMassageNotifier(jobCard.getWorkOrderNumber() + "'s Job Completed by Specialist");

            historyService.addSpecialist(HistoryDto.builder()
                    .tableID("Job Card table")
                    .description("Job Card status updated")
                    .rowName("Status")
                    .oldValue(jobCard.getSTATUS().name())
                    .newValue(specialist_jobCard.getSTATUS().name())
                    .updatedBy(specialist_jobCard.getUpdUser())
                    .updTime(Instant.now())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<JobCardDto> getAll() {
        Optional<List<JobCard>> all = specialistJobCardRepository.getAll();
        if (all.isEmpty())
            throw new UsernameNotFoundException("specialist job card not found");
        List<JobCard> specialistJobCards = all.get();
        return jobCardMapper.toDto(specialistJobCards);
    }

    private JobCard getById(String id) {
        JobCard jobCardId = specialistJobCardRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new UsernameNotFoundException("job card not found, may be Invalid job card id");
        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
    }

    private List<JobCardDto> getByStatus(String status) {
        Optional<List<JobCard>> optional = specialistJobCardRepository.findBySTATUS(status);
        if (optional.isEmpty())
            throw new UsernameNotFoundException("job card not found by this status : " + status);
        List<JobCard> jobCards = optional.get();
        return jobCardMapper.toDto(jobCards);
    }


}

