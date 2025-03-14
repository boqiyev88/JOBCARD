package uz.uat.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.dto.*;
import uz.uat.backend.mapper.JobCardMapper;
import uz.uat.backend.model.City;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.PdfFile;

import uz.uat.backend.model.enums.Status;


import uz.uat.backend.repository.CityRepository;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.PDFfileRepository;
import uz.uat.backend.service.serviceIMPL.SpecialistServiceIM;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SpecialistService implements SpecialistServiceIM {


    private final JobCardMapper jobCardMapper;
    private final JobCarRepository specialistJobCardRepository;
    private final Notifier notifier;
    private final HistoryService historyService;
    private final CityRepository cityRepository;
    private final PDFfileRepository fileRepository;

    @Transactional
    @Override
    public void addJobCard(JobCardDto jobCardDto) {
        if (jobCardDto == null) {
            throw new UsernameNotFoundException("jobCardDto is null");
        }
        try {
            Optional<City> LEG = cityRepository.findById(jobCardDto.LEG());
            Optional<City> TO = cityRepository.findById(jobCardDto.TO());
            if (LEG.isEmpty() || TO.isEmpty()) {
                throw new UsernameNotFoundException("city not found");
            }
            JobCardDtoMapper dtoMapper = jobCardMapper.toDtoMapper(jobCardDto);
            JobCard specialist_jobCard = jobCardMapper.toEntity(dtoMapper);
            specialist_jobCard.setSTATUS(Status.NEW);
            specialist_jobCard.setLEG(LEG.get());
            specialist_jobCard.setTO(TO.get());
            JobCard jobCard = specialistJobCardRepository.save(specialist_jobCard);
//            notifier.SpecialistNotifier(saveSpecialist);
//            notifier.SpecialistMassageNotifier("New JobCard added");
            /// historyga yozildi
            historyService.addHistory(HistoryDto.builder()
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
            PdfFile pdfFile = fileRepository.save(PdfFile.builder()
                    .fileName(file.getName())
                    .data(file.getBytes())
                    .build());
            jobCard.setMainPlan(pdfFile);
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

            notifier.SpecialistNotifier(getAll());                /// real timeda specialist tablega jo'natildi
            notifier.TechnicianMassageNotifier("Sizda Tasdiqdan o'tmagan ish mavjud");    /// uvidemleniyaga ish reject bo'lgani haqida habar jo'natildi
            historyService.addHistory(HistoryDto.builder()
                    .tableID("Job Card table")
                    .description("Job Card status updated")
                    .rowName("Status")
                    .oldValue(jobCardId.getSTATUS().name())
                    .newValue(Status.REJECTED.name())
                    .updatedBy(specialist_jobCard.getUpdUser())
                    .updTime(Instant.now())
                    .build());
            notifier.TechnicianNotifier(getAll());               /// real timeda texnik tablega jo'natildi
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
            case 1 -> {
                return getByStatus(Status.NEW.name());
            }
            case 2 -> {
                return getByStatus(Status.PENDING.name());
            }
            case 3 -> {
                return getByStatus(Status.IN_PROCESS.name());
            }
            case 4 -> {
                return getByStatus(Status.CONFIRMED.name());
            }
            case 5 -> {
                return getByStatus(Status.COMPLETED.name());
            }
            case 6 -> {
                return getByStatus(Status.REJECTED.name());
            }
            default -> {
                return getAll();
            }
        }
    }

    private List<JobCardDto> getAll() {
        Optional<List<JobCard>> all = specialistJobCardRepository.getAll();
        if (all.isEmpty())
            throw new UsernameNotFoundException("specialist job card not found");
        List<JobCard> jobCards = all.get();
        return getJobCard(jobCards);
    }

    private List<JobCardDto> getJobCard(List<JobCard> jobCards) {
        List<JobCardDto> jobCardDtos = new ArrayList<>();
        for (JobCard jobCard : jobCards) {
            jobCardDtos.add(JobCardDto.builder()
                    .WorkOrderNumber(jobCard.getWorkOrderNumber())
                    .REG(jobCard.getREG())
                    .SerialNumber1(jobCard.getSerialNumber1())
                    .ENGINE_1(jobCard.getENGINE_1())
                    .SerialNumber2(jobCard.getSerialNumber2())
                    .ENGINE_2(jobCard.getENGINE_2())
                    .SerialNumber3(jobCard.getSerialNumber3())
                    .APU(jobCard.getAPU())
                    .SerialNumber4(jobCard.getSerialNumber4())
                    .BEFORELIGHT(jobCard.getBEFORELIGHT())
                    .FH(jobCard.getFH())
                    .LEG(jobCard.getLEG().getID())
                    .TO(jobCard.getTO().getID())
                    .DATE(jobCard.getDATE())
                    .build());
        }
        return jobCardDtos;
    }

    private void changed(String jobId, String status) {
        try {
            JobCard jobCard = getById(jobId);
            JobCard specialist_jobCard = specialistJobCardRepository.updateSTATUSById(status, jobCard.getId());
            notifier.SpecialistNotifier(getAll());


            notifier.TechnicianNotifier(getAll());
            notifier.TechnicianMassageNotifier(jobCard.getWorkOrderNumber() + "'s Job Completed by Specialist");

            historyService.addHistory(HistoryDto.builder()
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

    private JobCard getById(String id) {
        JobCard jobCardId = specialistJobCardRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new UsernameNotFoundException("job card not found, may be Invalid job card id");
        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
    }

    private List<JobCardDto> getByStatus(String status) {
        List<JobCard> jobCards = specialistJobCardRepository.findBySTATUS(status);
        if (jobCards.isEmpty())
            throw new UsernameNotFoundException("job card not found by this status : " + status);
        return getJobCard(jobCards);
    }


}

