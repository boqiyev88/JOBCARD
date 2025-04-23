package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyConflictException;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.*;
import uz.uat.backend.model.*;
import uz.uat.backend.model.enums.OperationStatus;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.model.enums.TableName;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.ServicesRepository;
import uz.uat.backend.repository.WorkRepository;
import uz.uat.backend.service.utils.UtilsService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        if (jobCard.getStatus().equals(Status.NEW)) {
            Set<String> requestedServiceIds = workDtos.stream()
                    .map(RequestWorkDto::service_id)
                    .collect(Collectors.toSet());
            List<Work> works = getWorkDto(workDtos, getServices(requestedServiceIds), jobCard);
            for (Work work : works) {
                work.setCreatedDate(Instant.now());
            }
            List<Work> saved = workRepository.saveAll(works);

            jobCard.setStatus(Status.PENDING);
            jobCard.setUpdTime(Instant.now());
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
        } else
            throw new MyConflictException("You are trying to log in with an invalid job status");
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

        if (jobCard.getStatus().equals(Status.NEW) || jobCard.getStatus().equals(Status.REJECTED)) {

            Set<String> requestedServiceIds = workDto.stream()
                    .map(RequestEditWork::service_id)
                    .collect(Collectors.toSet());

            List<Work> works = getEditWorkDto(workDto, getServices(requestedServiceIds), jobCard);
            List<Work> saved = workRepository.saveAll(works);
            jobCard.setStatus(Status.PENDING);
            JobCard save = jobCarRepository.save(jobCard);

//            notifier.SpecialistMassageNotifier("Work successfully updated");
//            notifier.JobCardNotifier(jobService.getAll(1));
//            notifier.TechnicianMassageNotifier("Work successfully updated");

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
        } else
            throw new MyConflictException("You are trying to log in with an invalid job status");

        return jobService.getAll(1);
    }


    @Transactional
    public ResponseWork delete(String workid, DeleteWorkDto deleteWorkDto) {
        Work work = utilsService.getWorkById(workid);
//        User user = utilsService.getAuthenticatedUser();
        work.setIsDeleted(1);
//        work.setDelUser(user.getId());
        work.setDelTime(Instant.now());
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

        return showWorksWithService(deleteWorkDto.jobid());
    }

    public ResponseWork showWorksWithService(String jobCardId) {
        JobCard jobCard = utilsService.getJobById(jobCardId);
        List<Work> works = workRepository.findByJobcard_id(jobCard.getId());
        if (works.isEmpty())
            throw new MyNotFoundException("works not found, may be Invalid jobid");
        ResponseJobCardDto responseJobCard = utilsService.getJobCard(jobCard);
        return ResponseWork.builder()
                .jobcard(responseJobCard)
                .work(works.stream().map(work -> utilsService.getWork(work, work.getService_id())).collect(Collectors.toList()))
                .build();
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

    private Map<String, Services> getServices(Set<String> requestedServiceIds) {
        List<Services> list = servicesRepository.findAllByIds(requestedServiceIds);

        Set<String> foundIds = list.stream()
                .map(Services::getId)
                .collect(Collectors.toSet());

        List<String> missingServices = requestedServiceIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingServices.isEmpty())
            throw new MyNotFoundException("Service not found by ids");

        return list.stream().collect(Collectors.toMap(Services::getId, services -> services));
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

    private Map<String, Services> servicesMap(List<Services> servicesList) {
        return servicesList.stream().collect(Collectors.toMap(Services::getId, services -> services));
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
                        .status(Status.NEW)
                        .build())
                .collect(Collectors.toList());
    }

}
