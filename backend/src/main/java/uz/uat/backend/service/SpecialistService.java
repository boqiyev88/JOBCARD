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
import uz.uat.backend.model.*;
import uz.uat.backend.model.enums.OperationStatus;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.model.enums.TableName;
import uz.uat.backend.repository.*;
import uz.uat.backend.service.serviceIMPL.SpecialistServiceIM;
import uz.uat.backend.service.utils.UtilsService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SpecialistService implements SpecialistServiceIM {


    private final JobCardMapper jobCardMapper;
    private final JobCarRepository jobCardRepository;
    private final Notifier notifier;
    private final WorkRepository workRepository;
    private final ServicesRepository servicesRepository;
    private final HistoryService historyService;
    private final CityRepository cityRepository;
    private final PDFfileRepository fileRepository;
    private final UtilsService utilsService;

    @Transactional
    @Override
    public ResponseJobCardDto addJobCard(RequestJobCardDto jobCardDto) {
        if (jobCardDto == null) {
            throw new MyNotFoundException("jobCardDto is null");
        }
        Optional<City> LEG = cityRepository.findById(jobCardDto.leg());
        Optional<City> TO = cityRepository.findById(jobCardDto.to());
        if (LEG.isEmpty() || TO.isEmpty()) {
            throw new MyNotFoundException("city not found");
        }
        Optional<JobCard> optional = jobCardRepository.findByWorkOrderNumber(jobCardDto.work_order());

        if (optional.isPresent()) {
            throw new MyConflictException("jobCard already exists");
        }

        JobCardDtoMapper dtoMapper = jobCardMapper.toDtoMapper(jobCardDto);
        JobCard specialist_jobCard = jobCardMapper.toEntity(dtoMapper);
        specialist_jobCard.setStatus(Status.NEW);
        specialist_jobCard.setLeg(LEG.get());
        specialist_jobCard.setTo(TO.get());
        specialist_jobCard.setDate(jobCardDto.date());
        JobCard jobCard = jobCardRepository.save(specialist_jobCard);
        notifier.SpecialistMassageNotifier("New JobCard added");
        /// historyga yozildi
        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(specialist_jobCard.toString())
                .OS(OperationStatus.CREATED.name())
                .rowName("Status")
                .oldValue(" ")
                .newValue(Status.NEW.name())
                .updatedBy(jobCard.getUpdUser())
                .updTime(Instant.now())
                .build());

        notifier.TechnicianMassageNotifier("New JobCard added");

        return utilsService.getJobCard(jobCard);

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
            jobCardRepository.save(jobCard);
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
        JobCard specialist_jobCard = jobCardRepository.save(jobCardId);
        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(jobCardId.getId())
                .OS(OperationStatus.UPDATED.name())
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
        int validPage = page <= 0 ? 0 : page - 1;
        return switch (statusDto.status()) {
            case 1 -> changed(statusDto.jobId(), Status.NEW, 1, validPage);
            case 2 -> changed(statusDto.jobId(), Status.PENDING, 2, validPage);
            case 3 -> changed(statusDto.jobId(), Status.IN_PROCESS, 3, validPage);
            case 4 -> changed(statusDto.jobId(), Status.CONFIRMED, 4, validPage);
            case 5 -> changed(statusDto.jobId(), Status.COMPLETED, 5, validPage);
            case 6 -> changed(statusDto.jobId(), Status.REJECTED, 6, validPage);
            default -> getAll(0);
        };
    }

    @Override
    public PdfFile getPdfFromJob(String jobId) {
        JobCard jobCardId = jobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found");
        return getFile(jobCardId);
    }

    @Override
    public ResponseDto delete(@NotNull String jobId) {
        JobCard jobCard = jobCardRepository.findByJobCardId(jobId);
        if (jobId == null || jobId.isEmpty() || jobCard == null) {
            throw new MyNotFoundException("job card not found by this id:{} " + jobId);
        }
        jobCard.setIsDeleted(1);
        jobCardRepository.save(jobCard);
        return getAll(0);
    }

    @Override
    @Transactional
    public ResponseDto edit(String jobId, JobCardDto jobCardDto) {
        JobCard jobCardId = jobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null) {
            throw new MyNotFoundException("job card not found by this id: {}" + jobId);
        }
        Optional<City> LEG = cityRepository.findById(jobCardDto.leg());
        Optional<City> TO = cityRepository.findById(jobCardDto.to());
        if (LEG.isEmpty() || TO.isEmpty()) {
            throw new MyNotFoundException("city not found");
        }
        JobCard jobCard = editJobCard(jobCardId, jobCardDto, LEG.get(), TO.get());
        jobCardRepository.save(jobCard);
        return getAll(0);
    }

    @Override
    @Transactional
    public ResponseWork getWork(String workid) {
        Work work = getWorkById(workid);
        Services service = getServiceById(work.getService_id().getId());
        ResponseJobCardDto responseJobCard = utilsService.getJobCard(getById(work.getJobcard_id().getId()));
        List<ResponseWorkDto> responseWork = utilsService.getWorkDto(Collections.singletonList(work));
        return ResponseWork.builder()
                .job(responseJobCard)
                .work(responseWork)
                .tasks(service.getTasks())
                .build();
    }


    @Override
    @Transactional
    public ResponsesDtos getJobWithAll(String jobid, int page) {
        int validPage = page <= 0 ? 0 : page - 1;
        JobCard jobCard = getById(jobid);
        Page<Work> workPage = workRepository.findByJobcard_id(jobid, PageRequest.of(validPage, 10));
        List<Work> workList = workPage.getContent();
        List<Services> servicesList = new ArrayList<>();
        for (Work work : workList) {
            servicesList.add(work.getService_id());
        }
        ResponseJobCardDto responseJobCard = utilsService.getJobCard(jobCard);
        List<ResponseWorkDto> responseWork = utilsService.getWorkDto(workList);
        List<ResponseService> responseServices = utilsService.getTasks(servicesList);
        return ResponsesDtos.builder()
                .page(validPage + 1)
                .total(workPage.getTotalElements())
                .data(ResponseWork.builder()
                        .job(responseJobCard)
                        .work(responseWork)
                        .tasks(responseServices)
                        .build())
                .build();
    }


    public ResponseDto getByStatusNum(int status, int page, String search) {
        int validPage = page <= 0 ? 0 : page - 1;
        boolean isValidStatus = status > 0 && status < 7;
        boolean hasSearch = (search != null && !search.trim().isEmpty());

        if (!isValidStatus && !hasSearch) {
            return getAll(validPage);
        }
        if (!isValidStatus) {
            return getBySearch(search, validPage);
        }
        Status selectedStatus = switch (status) {
            case 1 -> Status.NEW;
            case 2 -> Status.PENDING;
            case 3 -> Status.IN_PROCESS;
            case 4 -> Status.CONFIRMED;
            case 5 -> Status.COMPLETED;
            case 6 -> Status.REJECTED;
            default -> null;
        };
        if (hasSearch) {
            return getByStatusAndSearch(selectedStatus, search, validPage);
        }
        return getByStatus(selectedStatus, validPage);
    }

    private Work getWorkById(String id) {
        Optional<Work> optional = workRepository.findById(id);
        if (optional.isEmpty())
            throw new MyNotFoundException("work not found by this id: {}" + id);
        return optional.get();
    }

    private Services getServiceById(String id) {
        Optional<Services> optional = servicesRepository.findById(id);
        if (optional.isEmpty())
            throw new MyNotFoundException("work not found by this id: {}" + id);
        return optional.get();
    }

    private JobCard editJobCard(JobCard jobCardId, JobCardDto jobCardDto, City LEG, City TO) {
        jobCardId.setWork_order(jobCardDto.work_order() != null ? jobCardDto.work_order() : jobCardId.getWork_order());
        jobCardId.setReg(jobCardDto.reg() != null ? jobCardDto.reg() : jobCardId.getReg());
        jobCardId.setSerial_number1(jobCardDto.serial_number1() != null ? jobCardDto.serial_number1() : jobCardId.getSerial_number1());
        jobCardId.setEngine_1(jobCardDto.engine_1() != null ? jobCardDto.engine_1() : jobCardId.getEngine_1());
        jobCardId.setSerial_number2(jobCardDto.serial_number2() != null ? jobCardDto.serial_number2() : jobCardId.getSerial_number2());
        jobCardId.setEngine_2(jobCardDto.engine_2() != null ? jobCardDto.engine_2() : jobCardId.getEngine_2());
        jobCardId.setSerial_number3(jobCardDto.serial_number3() != null ? jobCardDto.serial_number3() : jobCardId.getSerial_number3());
        jobCardId.setApu(jobCardDto.apu() != null ? jobCardDto.apu() : jobCardId.getApu());
        jobCardId.setSerial_number4(jobCardDto.serial_number4() != null ? jobCardDto.serial_number4() : jobCardId.getSerial_number4());
        jobCardId.setBefore_flight(jobCardDto.before_flight() != null ? jobCardDto.before_flight() : jobCardId.getBefore_flight());
        jobCardId.setFh(jobCardDto.fh() != null ? jobCardDto.fh() : jobCardId.getFh());
        jobCardId.setLeg(LEG);
        jobCardId.setTo(TO);
        jobCardId.setDate(jobCardDto.date() != null ? jobCardDto.date() : jobCardId.getDate());
        return jobCardId;
    }

    private ResponseDto getByStatusAndSearch(Status selectedStatus, String search, int page) {
        Page<JobCard> jobCards = jobCardRepository.findByStatusAndSearch(selectedStatus, search, PageRequest.of(page, 10));
        if (jobCards.isEmpty()) {
            return getStatusCount(ResponseDto.builder()
                    .page(1)
                    .total(jobCards.getTotalElements())
                    .data(new ArrayList<>())
                    .build());
        }
        return getStatusCount(ResponseDto.builder()
                .page(page + 1)
                .total(jobCards.getTotalElements())
                .data(utilsService.getJobCards(jobCards.getContent()))
                .build());
    }


    private ResponseDto getBySearch(String search, int page) {
        Page<JobCard> jobCards = jobCardRepository.findBySearch(search, PageRequest.of(page - 1, 10));
        if (jobCards.isEmpty()) {
            return getStatusCount(ResponseDto.builder()
                    .page(1)
                    .total(jobCards.getTotalElements())
                    .data(new ArrayList<>())
                    .build());
        }
        return getStatusCount(ResponseDto.builder()
                .page(page)
                .total(jobCards.getTotalElements())
                .data(utilsService.getJobCards(jobCards.getContent()))
                .build());
    }

    private ResponseDto getAll(int page) {
        Page<JobCard> all = jobCardRepository.getAll(PageRequest.of(page, 10));
        if (all.isEmpty())
            getStatusCount(ResponseDto.builder()
                    .page(page + 1)
                    .total(all.getTotalElements())
                    .data(utilsService.getJobCards(all.getContent()))
                    .build());

        return getStatusCount(ResponseDto.builder()
                .page(page + 1)
                .total(all.getTotalElements())
                .data(utilsService.getJobCards(all.getContent()))
                .build());
    }

    private ResponseDto changed(String jobId, Status status, int check, int page) {
        if (isValid(status, check)) {
            throw new MyConflictException("invalid change status " + status);
        }

        JobCard jobCard = getById(jobId);
        Status oldStatus = jobCard.getStatus();
        jobCard.setStatus(status);
        JobCard specialist_jobCard = jobCardRepository.save(jobCard);

        notifier.JobCardNotifier(getAll(page));
        notifier.TechnicianMassageNotifier(jobCard.getId() + "'s Job Completed by Specialist");

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(jobCard.getId())
                .OS(OperationStatus.UPDATED.name())
                .rowName("Status")
                .oldValue(oldStatus.name())
                .newValue(specialist_jobCard.getStatus().name())
                .updatedBy(specialist_jobCard.getUpdUser())
                .updTime(Instant.now())
                .build());
        return getAll(page);
    }

    private PdfFile getFile(JobCard jobCard) {
        if (jobCard.getMainPlan() == null)
            return new PdfFile();
        Optional<PdfFile> optional = fileRepository.findById(String.valueOf(jobCard.getMainPlan().getId()));
        if (optional.isEmpty())
            throw new MyNotFoundException("file not found");
        return optional.get();
    }

    private JobCard getById(String id) {
        JobCard jobCardId = jobCardRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found, may be Invalid job card id");
        return jobCardId;
    }

    private ResponseDto getByStatus(Status status, int page) {
        Page<JobCard> jobCards = jobCardRepository.findBySTATUS(status, PageRequest.of(page, 10));
        if (jobCards.isEmpty()) {
            return getStatusCount(ResponseDto.builder()
                    .page(1)
                    .total(jobCards.getTotalElements())
                    .data(new ArrayList<>())
                    .build());
        }
        return getStatusCount(ResponseDto.builder()
                .page(page + 1)
                .total(jobCards.getTotalElements())
                .data(utilsService.getJobCards(jobCards.getContent()))
                .build());

    }

    private ResponseDto getStatusCount(ResponseDto responseDto) {
        List<StatusCountDto> dto = jobCardRepository.getByStatusCount();
        Long newCount = dto.stream().filter(d -> "NEW".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long pendingCount = dto.stream().filter(d -> "PENDING".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long inProcessCount = dto.stream().filter(d -> "IN_PROCESS".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long confirmedCount = dto.stream().filter(d -> "CONFIRMED".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long completedCount = dto.stream().filter(d -> "COMPLETED".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        Long rejectedCount = dto.stream().filter(d -> "REJECTED".equals(d.getStatus())).map(StatusCountDto::getCount).findFirst().orElse(0L);
        return ResponseDto.builder()
                .page(responseDto.page())
                .total(responseDto.total())
                .data(responseDto.data())
                .all(newCount + pendingCount + inProcessCount + confirmedCount + completedCount + rejectedCount)
                .New(newCount)
                .Pending(pendingCount)
                .In_process(inProcessCount)
                .Confirmed(confirmedCount)
                .Completed(completedCount)
                .Rejected(rejectedCount)
                .build();
    }

    private boolean isValid(Status oldStatus, int newStatus) {
        return switch (newStatus) {
            case 3 -> oldStatus == Status.PENDING; // IN_PROCESS ga faqat PENDING dan o‘tishi mumkin
            case 5, 6 -> oldStatus == Status.CONFIRMED; // COMPLETED yoki REJECTED faqat CONFIRMED dan o‘tishi mumkin
            default -> false;
        };
    }


}

