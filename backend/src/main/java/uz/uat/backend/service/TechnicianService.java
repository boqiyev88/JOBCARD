package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.PdfFile;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.enums.OperationStatus;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.model.enums.TableName;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.ServicesRepository;
import uz.uat.backend.repository.WorkRepository;
import uz.uat.backend.service.utils.UtilsService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final JobCarRepository jobCarRepository;
    private final WorkRepository workRepository;
    private final ServicesRepository servicesRepository;
    private final Notifier notifier;
    private final JobService jobService;
    private final HistoryService historyService;
    private final UtilsService utilsService;


    @Transactional
    public ResponsesDtos addWork(List<RequestWorkDto> workDtos, String jobCard_id) {
        JobCard jobCard = utilsService.getJobById(jobCard_id);
        Map<String, Services> servicesMap = servicesMap();
        List<Work> works = getWorkDto(workDtos, servicesMap, jobCard);
        List<Work> saved = workRepository.saveAll(works);

        jobCard.setStatus(Status.PENDING);
        JobCard save = jobCarRepository.save(jobCard);

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.work.name())
                .tableID(" ")
                .OS(OperationStatus.CREATED.name())
                .rowName(" ")
                .oldValue(" ")
                .newValue(utilsService.getWork(saved).toString())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(jobCard.getId())
                .OS(OperationStatus.CREATED.name())
                .rowName("Status")
                .oldValue(Status.NEW.name())
                .newValue(jobCard.getStatus().name())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );

//        notifier.SpecialistMassageNotifier("Work added successfully");
//        notifier.JobCardNotifier(getAll(1));
//        notifier.TechnicianMassageNotifier("Work added successfully");
        return getWorkList(1);
    }


    public ResponseDto getByStatusNum(int status, int page, String search) {
        return jobService.getByStatusNum(status, page, search);
    }


    public PdfFile getPdfFromJob(String jobId) {
        return jobService.getPdfFromJob(jobId);
    }

    @Transactional
    public ResponseDto edit(String jobid, List<RequestEditWork> workDto) {
        JobCard jobCard = utilsService.getJobById(jobid);
        Map<String, Services> servicesMap = servicesMap();
        List<Work> works = getEditWorkDto(workDto, servicesMap, jobCard);
        List<Work> saved = workRepository.saveAll(works);
        jobCard.setStatus(Status.PENDING);
        JobCard save = jobCarRepository.save(jobCard);

        notifier.SpecialistMassageNotifier("Work successfully updated");
        notifier.JobCardNotifier(jobService.getAll(1));
        notifier.TechnicianMassageNotifier("Work successfully updated");

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.work.name())
                .tableID(jobid)
                .OS(OperationStatus.UPDATED.name())
                .rowName("Status")
                .oldValue(workDto.toString())
                .newValue(utilsService.getWork(saved).toString())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(jobCard.getId())
                .OS(OperationStatus.UPDATED.name())
                .rowName("Status")
                .oldValue(jobCard.getStatus().name())
                .newValue(save.getStatus().name())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );

        return jobService.getAll(1);
    }


    private ResponsesDtos getWorkList(int page) {
        Page<Work> works = workRepository.getAll(PageRequest.of(page - 1, 10));
        if (works.isEmpty()) {
            return ResponsesDtos.builder()
                    .page(1)
                    .total(works.getTotalElements())
                    .data(utilsService.getWork(works.getContent()))
                    .build();
        }
        return ResponsesDtos.builder()
                .page(page)
                .total(works.getTotalElements())
                .data(utilsService.getWork(works.getContent()))
                .build();
    }

    private List<Services> getServices() {
        List<Services> optional = servicesRepository.getAll();
        if (optional.isEmpty())
            throw new MyNotFoundException("service not found, may be Invalid service jobid");
        return optional;
    }


    private List<Work> getWorkDto(List<RequestWorkDto> workDtoList, Map<String, Services> servicesMap, JobCard jobCard) {
        return workDtoList.stream()
                .map(dto -> Work.builder()
                        .service_id(servicesMap.get(dto.service_id()))
                        .jobcard_id(jobCard)
                        .threshold(dto.threshold())
                        .repeat_int(dto.repeat_int())
                        .zone(dto.zone())
                        .mpr(dto.mpr())
                        .access(dto.access())
                        .airplane_app(dto.airplane_app())
                        .description(dto.description())
                        .access_note(dto.access_note())
                        .task_description(dto.task_description())
                        .dit(dto.dit() ? 1 : 0)
                        .avionic(dto.avionic() ? 1 : 0)
                        .mechanic(dto.mechanic() ? 1 : 0)
                        .cab_mechanic(dto.cab_mechanic() ? 1 : 0)
                        .sheet_metal(dto.sheet_metal() ? 1 : 0)
                        .ndt(dto.ndt() ? 1 : 0)
                        .build())
                .collect(Collectors.toList());
    }

    private Map<String, Services> servicesMap() {
        return getServices().stream()
                .collect(Collectors.toMap(Services::getId, Function.identity()));
    }

    private List<Work> getEditWorkDto(List<RequestEditWork> workDtoList, Map<String, Services> servicesMap, JobCard jobCard) {
        return workDtoList.stream()
                .map(dto -> Work.builder()
                        .id(dto.workid() != null ? dto.workid() : null)
                        .service_id(servicesMap.get(dto.service_id()))
                        .jobcard_id(jobCard)
                        .threshold(dto.threshold())
                        .repeat_int(dto.repeat_int())
                        .zone(dto.zone())
                        .mpr(dto.mpr())
                        .access(dto.access())
                        .airplane_app(dto.airplane_app())
                        .description(dto.description())
                        .access_note(dto.access_note())
                        .task_description(dto.task_description())
                        .dit(dto.dit() ? 1 : 0)
                        .avionic(dto.avionic() ? 1 : 0)
                        .mechanic(dto.mechanic() ? 1 : 0)
                        .cab_mechanic(dto.cab_mechanic() ? 1 : 0)
                        .sheet_metal(dto.sheet_metal() ? 1 : 0)
                        .ndt(dto.ndt() ? 1 : 0)
                        .build())
                .collect(Collectors.toList());
    }

    public ResponseDto delete(String workid) {
        Work work = utilsService.getWorkById(workid);
        work.setIsDeleted(1);
        Work save = workRepository.save(work);

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.work.name())
                .tableID(work.getId())
                .OS(OperationStatus.DELETED.name())
                .rowName("table")
                .oldValue(utilsService.getWork(work).toString())
                .newValue(utilsService.getWork(save).toString())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build());

        return jobService.getAll(1);
    }

    public ResponseWork showWorksWithService(String jobCardId) {
        JobCard jobCard = utilsService.getJobById(jobCardId);
        List<Work> works = workRepository.findByJobcard_id(jobCard.getId());
        if (works.isEmpty())
            throw new MyNotFoundException("works not found, may be Invalid jobid");
        return ResponseWork.builder()
                .job_status(String.valueOf(utilsService.getStatus(jobCard.getStatus())))
                .work(works.stream().map(work -> utilsService.getWork(work, work.getService_id())).collect(Collectors.toList()))
                .build();
    }
}
