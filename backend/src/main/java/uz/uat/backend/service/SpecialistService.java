package uz.uat.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyConflictException;
import uz.uat.backend.config.exception.MyNotFoundException;
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
    public List<ResponseJobCardDto> addJobCard(JobCardDto jobCardDto) {
        if (jobCardDto == null) {
            throw new MyNotFoundException("jobCardDto is null");
        }
        Optional<City> LEG = cityRepository.findById(jobCardDto.leg());
        Optional<City> TO = cityRepository.findById(jobCardDto.to());
        if (LEG.isEmpty() || TO.isEmpty()) {
            throw new MyNotFoundException("city not found");
        }
        Optional<JobCard> optional =
                specialistJobCardRepository.findByWorkOrderNumber(jobCardDto.workOrder());

        if (optional.isPresent()) {
            throw new MyConflictException("jobCard already exists");
        }

        JobCardDtoMapper dtoMapper = jobCardMapper.toDtoMapper(jobCardDto);
        JobCard specialist_jobCard = jobCardMapper.toEntity(dtoMapper);
        specialist_jobCard.setStatus(Status.NEW);
        specialist_jobCard.setLeg(LEG.get());
        specialist_jobCard.setTo(TO.get());
        JobCard jobCard = specialistJobCardRepository.save(specialist_jobCard);
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

//            notifier.JobCardNotifier(saveTechnician);
//            notifier.TechnicianMassageNotifier("New JobCard added");

        return getAll();
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
            throw new MyConflictException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public void returned(RequestDto requestDto) {
        if (requestDto.id() == null || requestDto.massage() == null)
            throw new MyNotFoundException("invalid request id or massage is null");

        JobCard jobCardId = getById(requestDto.id());

        jobCardId.setStatus(Status.REJECTED);
        JobCard specialist_jobCard = specialistJobCardRepository.save(jobCardId);
        historyService.addHistory(HistoryDto.builder()
                .tableID("Job Card table")
                .description("Job Card status updated")
                .rowName("Status")
                .oldValue(jobCardId.getStatus().name())
                .newValue(Status.REJECTED.name())
                .updatedBy(specialist_jobCard.getUpdUser())
                .updTime(Instant.now())
                .build());


//        notifier.SpecialistNotifier(getAll());                /// real timeda specialist tablega jo'natildi
//        notifier.TechnicianMassageNotifier("Sizda Tasdiqdan o'tmagan ish mavjud");    /// uvidemleniyaga ish reject bo'lgani haqida habar jo'natildi
//        notifier.JobCardNotifier(getAll());               /// real timeda texnik tablega jo'natildi

    }


    @Transactional
    @Override
    public void changeStatus(RequestStatusDto statusDto) {
        switch (statusDto.status()) {
            case 1: {
                changed(statusDto.jobId(), Status.NEW, 1);
                break;
            }
            case 2: {
                changed(statusDto.jobId(), Status.PENDING, 2);
                break;
            }
            case 3: {
                changed(statusDto.jobId(), Status.IN_PROCESS, 3);
                break;
            }
            case 4: {
                changed(statusDto.jobId(), Status.CONFIRMED, 4);
                break;
            }
            case 5: {
                changed(statusDto.jobId(), Status.COMPLETED, 5);
                break;
            }
            case 6: {
                changed(statusDto.jobId(), Status.REJECTED, 6);
                break;
            }
        }
    }

    @Override
    public PdfFile getPdfFromJob(String jobId) {
        JobCard jobCardId = specialistJobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found");
        return jobCardId.getMainPlan();
    }


    public List<ResponseJobCardDto> getByStatusNum(int status) {
        switch (status) {
            case 1 -> {
                return getByStatus(Status.NEW);
            }
            case 2 -> {
                return getByStatus(Status.PENDING);
            }
            case 3 -> {
                return getByStatus(Status.IN_PROCESS);
            }
            case 4 -> {
                return getByStatus(Status.CONFIRMED);
            }
            case 5 -> {
                return getByStatus(Status.COMPLETED);
            }
            case 6 -> {
                return getByStatus(Status.REJECTED);
            }
            default -> {
                return getAll();
            }
        }
    }

    public List<ResponseJobCardDto> getAll() {
        List<JobCard> all = specialistJobCardRepository.getAll();
        if (all.isEmpty())
            throw new MyNotFoundException("job card is empty");
        return getJobCard(all);
    }

    private void changed(String jobId, Status status, int check) {

        if (!isValid(status, check)) {
            throw new MyConflictException("invalid change status " + status);
        }

        JobCard jobCard = getById(jobId);
        Status oldStatus = jobCard.getStatus();
        jobCard.setStatus(status);
        JobCard specialist_jobCard = specialistJobCardRepository.save(jobCard);

//        notifier.JobCardNotifier(getAll());
//        notifier.TechnicianMassageNotifier(jobCard.getWorkOrderNumber() + "'s Job Completed by Specialist");

        historyService.addHistory(HistoryDto.builder()
                .tableID("Job Card table")
                .description("Job Card status updated")
                .rowName("Status")
                .oldValue(oldStatus.name())
                .newValue(specialist_jobCard.getStatus().name())
                .updatedBy(specialist_jobCard.getUpdUser())
                .updTime(Instant.now())
                .build());


    }

    private JobCard getById(String id) {
        JobCard jobCardId = specialistJobCardRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found, may be Invalid job card id");
        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
    }

    private List<ResponseJobCardDto> getByStatus(Status status) {
        List<JobCard> jobCards = specialistJobCardRepository.findBySTATUS(status);
        if (jobCards.isEmpty())
            throw new MyNotFoundException("job card not found by this status : " + status);
        return getJobCard(jobCards);
    }


    private boolean isValid(Status oldStatus, int newStatus) {
        return switch (newStatus) {
            case 3 -> oldStatus == Status.PENDING; // IN_PROCESS ga faqat PENDING dan o‘tishi mumkin
            case 5, 6 -> oldStatus == Status.CONFIRMED; // COMPLETED yoki REJECTED faqat CONFIRMED dan o‘tishi mumkin
            default -> false;
        };
    }

    private List<ResponseJobCardDto> getJobCard(List<JobCard> jobCards) {
        List<ResponseJobCardDto> jobCardDtos = new ArrayList<>();
        for (JobCard jobCard : jobCards) {
            jobCardDtos.add(ResponseJobCardDto.builder()
                    .workOrder(jobCard.getWorkOrder())
                    .reg(jobCard.getReg())
                    .serialNumber1(jobCard.getSerialNumber1())
                    .engine_1(jobCard.getEngine_1())
                    .serialNumber2(jobCard.getSerialNumber2())
                    .engine_2(jobCard.getEngine_2())
                    .serialNumber3(jobCard.getSerialNumber3())
                    .apu(jobCard.getApu())
                    .serialNumber4(jobCard.getSerialNumber4())
                    .beforeLight(jobCard.getBeforelight())
                    .fh(jobCard.getFh())
                    .leg(jobCard.getLeg().getId())
                    .to(jobCard.getTo().getId())
                    .date(jobCard.getDate())
                    .status(jobCard.getStatus().name())
                    .build());
        }
        return jobCardDtos;
    }

}

