package uz.uat.backend.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SpecialistService implements SpecialistServiceIM {


    private final JobCardMapper jobCardMapper;
    private final JobCarRepository jobCardRepository;
    private final Notifier notifier;
    private final WorkRepository workRepository;
    private final HistoryService historyService;
    private final CityRepository cityRepository;
    private final PDFfileRepository fileRepository;
    private final UtilsService utilsService;
    private final JobService jobService;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public ResponseJobCardDto addJobCard(RequestJobCardDto jobCardDto) {
        if (jobCardDto == null) {
            throw new MyNotFoundException("jobCardDto is null");
        }

        Optional<City> LEG = cityRepository.findById(jobCardDto.leg());
        Optional<City> TO = cityRepository.findById(jobCardDto.to());
        if (LEG.isEmpty() || TO.isEmpty()) {
            throw new MyNotFoundException("city not found");
        }
//        Optional<JobCard> optional = jobCardRepository.findByWorkOrderNumber(jobCardDto.work_order());
//
//        if (optional.isPresent()) {
//            throw new MyConflictException("jobCard already exists");
//        }

        JobCardDtoMapper dtoMapper = jobCardMapper.toDtoMapper(jobCardDto);
        JobCard specialist_jobCard = jobCardMapper.toEntity(dtoMapper);
        specialist_jobCard.setStatus(Status.NEW);
        specialist_jobCard.setLeg(LEG.get());
        specialist_jobCard.setTo(TO.get());
        specialist_jobCard.setDate(jobCardDto.date());
        specialist_jobCard.setCreatedDate(Instant.now());
        JobCard jobCard = jobCardRepository.save(specialist_jobCard);
//        notifier.SpecialistMassageNotifier("New JobCard added",utilsService.getUser(jobCard.getInsUser()));
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

//        notifier.TechnicianMassageNotifier("New JobCard added");
        return utilsService.getJobCard(jobCard, new Message());

    }

    @Override
    @Transactional
    public ResponseDto addFileToJob(String jobId, MultipartFile file) {
        JobCard jobCard = utilsService.getJobById(jobId);
        PdfFile existingFile = jobCard.getMainPlan();
        try {

            if (existingFile != null &&
                    existingFile.getFileName().equals(file.getOriginalFilename()) &&
                    Arrays.equals(existingFile.getData(), file.getBytes())) {
                jobCard.setUpdTime(Instant.now());
                jobCardRepository.save(jobCard);
                return jobService.getAll(0);
            }

            if (existingFile != null && !existingFile.getFileName().equals(file.getOriginalFilename())
                    && !Arrays.equals(existingFile.getData(), file.getBytes())) {
                existingFile.setIsDeleted(1);
                fileRepository.save(existingFile);
                PdfFile newFile = fileRepository.save(PdfFile.builder()
                        .fileName(file.getOriginalFilename())
                        .data(file.getBytes())
                        .build());

                jobCard.setMainPlan(newFile);
                jobCard.setUpdTime(Instant.now());
                jobCardRepository.save(jobCard);
                return jobService.getAll(0);
            }

            PdfFile newFile = fileRepository.save(PdfFile.builder()
                    .fileName(file.getOriginalFilename())
                    .data(file.getBytes())
                    .build());

            jobCard.setMainPlan(newFile);
            jobCard.setUpdTime(Instant.now());
            jobCardRepository.save(jobCard);

        } catch (IOException e) {
            throw new MyConflictException("Faylni o‘qishda xatolik: " + e.getMessage());
        }

        return jobService.getAll(0);
    }

    @Override
    @Transactional
    public ResponseDto returned(RequestDto requestDto) {
        if (requestDto.jobid() == null || requestDto.message() == null)
            throw new MyNotFoundException("invalid request jobid or message is null");

        JobCard jobCardId = utilsService.getJobById(requestDto.jobid());

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
        return jobService.getAll(1);
    }

    @Transactional
    @Override
    public ResponseDto changeStatus(RequestStatusDto statusDto) {
        JobCard jobCard = utilsService.getJobById(statusDto.jobId());

        if (!isValid(jobCard.getStatus(), statusDto.status()))
            throw new MyConflictException("you are trying change to invalid status");

        return switch (statusDto.status()) {
            case 3 -> changed(jobCard, Status.IN_PROCESS);
            case 5 -> changed(jobCard, Status.REJECTED);
            case 6 -> changed(jobCard, Status.COMPLETED);
            default -> jobService.getAll(0);
        };


    }


    @Override
    public PdfFile getPdfFromJob(String jobId) {
        return jobService.getPdfFromJob(jobId);
    }

    @Override
    public ResponseDto delete(@NotNull String jobId) {
        JobCard jobCard = jobCardRepository.findByJobCardId(jobId);
        if (jobId == null || jobId.isEmpty() || jobCard == null) {
            throw new MyNotFoundException("job card not found by this jobid:{} " + jobId);
        }
        jobCard.setIsDeleted(1);
        jobCard.setDelTime(Instant.now());
        jobCardRepository.save(jobCard);
        return jobService.getAll(0);
    }

    @Override
    @Transactional
    public ResponseDto edit(String jobId, JobCardDto jobCardDto) {
        JobCard jobCardId = jobCardRepository.findByJobCardId(jobId);
        if (jobCardId == null) {
            throw new MyNotFoundException("job card not found by this jobid: {}" + jobId);
        }
        Optional<City> LEG = cityRepository.findById(jobCardDto.leg());
        Optional<City> TO = cityRepository.findById(jobCardDto.to());
        if (LEG.isEmpty() || TO.isEmpty()) {
            throw new MyNotFoundException("city not found");
        }
        JobCard jobCard = editJobCard(jobCardId, jobCardDto, LEG.get(), TO.get());
        jobCard.setUpdTime(Instant.now());
        jobCardRepository.save(jobCard);
        return jobService.getAll(0);
    }

    /// work jobid orqali work ga tegishli barcha malumotlar
    @Override
    @Transactional
    public ResultJob getWork(String workid) {
        Work work = utilsService.getWorkById(workid);
        Services service = utilsService.getServiceById(work.getService_id().getId());
        Message message = utilsService.getMessage(work.getJobcard_id().getId());
        ResponseJobCardDto responseJobCard = utilsService.getJobCard(utilsService.getJobById(work.getJobcard_id().getId()),
                message != null ? message : new Message());
        ResponseWorkDto responseWork = utilsService.getWork(work);
        ResultJob resultJob = jobCardMapper.fromDto(responseJobCard);
        resultJob.setServices(utilsService.getTaskFromService(service));
        resultJob.setWork(responseWork);
        return resultJob;
    }


    /// job jobid orqali barcha work va servicelarni olish
    @Override
    @Transactional
    public List<ResultWork> getWorks(String jobid) {
        List<Work> workPage = workRepository.findByJobcard_id(jobid);
        if (workPage.isEmpty())
            return Collections.emptyList();
        return workPage.stream()
                .map(work -> new ResultWork(
                        work.getId(),
                        utilsService.fromEntityService(work.getService_id())
                ))
                .collect(Collectors.toList());
    }

    ///  job jobid orqali barcha worklarni olib kelish
    @Override
    @Transactional
    public ResultJob getJobWithAll(String jobid) {
        JobCard jobCard = utilsService.getJobById(jobid);
        List<Work> workList = workRepository.findByJobcard_id(jobid);
        Optional<Message> message = messageRepository.findByJobId(jobCard.getId());
        List<Services> servicesList = workList.stream()
                .map(Work::getService_id)
                .collect(Collectors.toList());

        ResponseJobCardDto responseJobCard = utilsService.getJobCard(jobCard, message.orElse(new Message()));
        ResultJob resultJob = jobCardMapper.fromDto(responseJobCard);
        List<ResponseWorkDto> responseWork = utilsService.getWorksDto(workList);
        List<ResponseService> responseServices = utilsService.getTasksFromService(servicesList);
        resultJob.setServices(responseServices);
        resultJob.setWork(responseWork);
        return resultJob;
    }

    @Override
    public ResponseJobCardDto getJob(String jobId) {
        JobCard jobCard = utilsService.getJobById(jobId);
        Optional<Message> message = messageRepository.findByJobId(jobCard.getId());
        return utilsService.getJobCard(jobCard, message.orElse(new Message()));
    }

    public ResponseDto getByStatusNum(int status, int page, String search) {
        return jobService.getByStatusNum(status, page, search);
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

    private ResponseDto changed(JobCard jobCard, Status newStatus) {
        Status oldStatus = jobCard.getStatus();
        jobCard.setStatus(newStatus);
        jobCard.setUpdTime(Instant.now());
        JobCard specialist_jobCard = jobCardRepository.save(jobCard);

        Message message = utilsService.getMessage(jobCard.getId());

        notifier.JobCardNotifier(jobService.getAll(0));
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
        ResponseDto responseDto = jobService.getAll(0);
        return ResponseDto.builder()
                .page(responseDto.page())
                .total(responseDto.total())
                .data(responseDto.data())
                .all(responseDto.all())
                .New(responseDto.New())
                .Pending(responseDto.Pending())
                .In_process(responseDto.In_process())
                .Confirmed(responseDto.Confirmed())
                .Completed(responseDto.Completed())
                .Rejected(responseDto.Rejected())
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

