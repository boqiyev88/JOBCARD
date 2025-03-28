package uz.uat.backend.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


import uz.uat.backend.model.enums.TableName;
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
    public ResponseJobCardDto addJobCard(JobCardDto jobCardDto) {
        if (jobCardDto == null) {
            throw new MyNotFoundException("jobCardDto is null");
        }
        Optional<City> LEG = cityRepository.findById(jobCardDto.leg());
        Optional<City> TO = cityRepository.findById(jobCardDto.to());
        if (LEG.isEmpty() || TO.isEmpty()) {
            throw new MyNotFoundException("city not found");
        }
        Optional<JobCard> optional =
                specialistJobCardRepository.findByWorkOrderNumber(jobCardDto.work_order());

        if (optional.isPresent()) {
            throw new MyConflictException("jobCard already exists");
        }

        JobCardDtoMapper dtoMapper = jobCardMapper.toDtoMapper(jobCardDto);
        JobCard specialist_jobCard = jobCardMapper.toEntity(dtoMapper);
        specialist_jobCard.setStatus(Status.NEW);
        specialist_jobCard.setLeg(LEG.get());
        specialist_jobCard.setTo(TO.get());
        specialist_jobCard.setDate(jobCardDto.date());
        JobCard jobCard = specialistJobCardRepository.save(specialist_jobCard);
        notifier.SpecialistMassageNotifier("New JobCard added");
        /// historyga yozildi
        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(specialist_jobCard.toString())
                .description("New Job Card added")
                .rowName("Status")
                .oldValue(" ")
                .newValue(Status.NEW.name())
                .updatedBy(jobCard.getUpdUser())
                .updTime(Instant.now())
                .build());

        notifier.TechnicianMassageNotifier("New JobCard added");

        return getJobCard(jobCard);
    }

    @Override
    @Transactional
    public ResponseDto addFileToJob(String jobId, MultipartFile file) {
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
        return getAll(1);
    }

    @Transactional
    @Override
    public ResponseDto returned(RequestDto requestDto) {
        if (requestDto.id() == null || requestDto.massage() == null)
            throw new MyNotFoundException("invalid request id or massage is null");

        JobCard jobCardId = getById(requestDto.id());

        jobCardId.setStatus(Status.REJECTED);
        JobCard specialist_jobCard = specialistJobCardRepository.save(jobCardId);
        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(jobCardId.getId())
                .description("Job Card status updated")
                .rowName("Status")
                .oldValue(jobCardId.getStatus().name())
                .newValue(specialist_jobCard.getStatus().name())
                .updatedBy(specialist_jobCard.getUpdUser())
                .updTime(Instant.now())
                .build());


//        notifier.SpecialistNotifier(getAll());                /// real timeda specialist tablega jo'natildi
//        notifier.TechnicianMassageNotifier("Sizda Tasdiqdan o'tmagan ish mavjud");    /// uvidemleniyaga ish reject bo'lgani haqida habar jo'natildi
//        notifier.JobCardNotifier(getAll());               /// real timeda texnik tablega jo'natildi
        return getAll(1);
    }

    @Transactional
    @Override
    public ResponseDto changeStatus(RequestStatusDto statusDto, int page) {
        return switch (statusDto.status()) {
            case 1 -> changed(statusDto.jobId(), Status.NEW, 1, page);
            case 2 -> changed(statusDto.jobId(), Status.PENDING, 2, page);
            case 3 -> changed(statusDto.jobId(), Status.IN_PROCESS, 3, page);
            case 4 -> changed(statusDto.jobId(), Status.CONFIRMED, 4, page);
            case 5 -> changed(statusDto.jobId(), Status.COMPLETED, 5, page);
            case 6 -> changed(statusDto.jobId(), Status.REJECTED, 6, page);
            default -> getAll(page);
        };
    }

    @Override
    public PdfFile getPdfFromJob(String jobId) {
        JobCard jobCardId = specialistJobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found");
        return jobCardId.getMainPlan();
    }

    @Override
    public ResponseDto delete(@NotNull String jobId) {
        if (jobId == null || jobId.isEmpty())
            throw new MyNotFoundException("invalid job id");
        JobCard jobCard = specialistJobCardRepository.findByJobCardId(jobId);
        jobCard.setIsDeleted(1);
        specialistJobCardRepository.save(jobCard);
        return getAll(1);
    }

    public ResponseDto getByStatusNum(int status, int page) {
        boolean isValidStatus = status > 0 && status < 7;
        int validPage = (page < 1) ? 0 : page;

        if (!isValidStatus)
            return getAll(validPage);

        switch (status) {
            case 1 -> {
                return getByStatus(Status.NEW, validPage);
            }
            case 2 -> {
                return getByStatus(Status.PENDING, validPage);
            }
            case 3 -> {
                return getByStatus(Status.IN_PROCESS, validPage);
            }
            case 4 -> {
                return getByStatus(Status.CONFIRMED, validPage);
            }
            case 5 -> {
                return getByStatus(Status.COMPLETED, validPage);
            }
            case 6 -> {
                return getByStatus(Status.REJECTED, validPage);
            }
            default -> {
                return getAll(validPage);
            }
        }
    }

    public ResponseDto getAll(int page) {
        Page<JobCard> all = specialistJobCardRepository.getAll(PageRequest.of(page - 1, 10));
        if (all.isEmpty())
            throw new MyNotFoundException("job card is empty");
        return ResponseDto.builder()
                .page(page)
                .total(all.getTotalElements())
                .data(getJobCards(all.getContent()))
                .build();
    }

    private ResponseDto changed(String jobId, Status status, int check, int page) {

        if (!isValid(status, check)) {
            throw new MyConflictException("invalid change status " + status);
        }

        JobCard jobCard = getById(jobId);
        Status oldStatus = jobCard.getStatus();
        jobCard.setStatus(status);
        JobCard specialist_jobCard = specialistJobCardRepository.save(jobCard);

        notifier.JobCardNotifier(getAll(page));
        notifier.TechnicianMassageNotifier(jobCard.getId() + "'s Job Completed by Specialist");

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(jobCard.getId())
                .description("Job Card status updated")
                .rowName("Status")
                .oldValue(oldStatus.name())
                .newValue(specialist_jobCard.getStatus().name())
                .updatedBy(specialist_jobCard.getUpdUser())
                .updTime(Instant.now())
                .build());
        return getAll(page);
    }

    private JobCard getById(String id) {
        JobCard jobCardId = specialistJobCardRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found, may be Invalid job card id");
        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
    }

    private ResponseDto getByStatus(Status status, int page) {
        Page<JobCard> jobCards = specialistJobCardRepository.findBySTATUS(status, PageRequest.of(page - 1, 10));
        if (jobCards.isEmpty())
            throw new MyNotFoundException("job card not found");
        return ResponseDto.builder()
                .page(page)
                .total(jobCards.getTotalElements())
                .data(getJobCards(jobCards.getContent()))
                .build();

    }


    private boolean isValid(Status oldStatus, int newStatus) {
        return switch (newStatus) {
            case 3 -> oldStatus == Status.PENDING; // IN_PROCESS ga faqat PENDING dan o‘tishi mumkin
            case 5, 6 -> oldStatus == Status.CONFIRMED; // COMPLETED yoki REJECTED faqat CONFIRMED dan o‘tishi mumkin
            default -> false;
        };
    }

    private ResponseJobCardDto getJobCard(JobCard jobCard) {
        return ResponseJobCardDto.builder()
                .work_order(jobCard.getWork_order())
                .reg(jobCard.getReg())
                .serial_number1(jobCard.getSerial_number1())
                .engine_1(jobCard.getEngine_1())
                .serial_number2(jobCard.getSerial_number2())
                .engine_2(jobCard.getEngine_2())
                .serial_number3(jobCard.getSerial_number3())
                .apu(jobCard.getApu())
                .serial_number4(jobCard.getSerial_number4())
                .before_flight(jobCard.getBefore_flight())
                .fh(jobCard.getFh())
                .leg(jobCard.getLeg().getId())
                .to(jobCard.getTo().getId())
                .date(jobCard.getDate())
                .status(jobCard.getStatus().name())
                .build();
    }

    private List<ResponseJobCardDto> getJobCards(List<JobCard> jobCards) {
        List<ResponseJobCardDto> jobCardDtos = new ArrayList<>();
        for (JobCard jobCard : jobCards) {
            jobCardDtos.add(getJobCard(jobCard));
        }
        return jobCardDtos;
    }

}

