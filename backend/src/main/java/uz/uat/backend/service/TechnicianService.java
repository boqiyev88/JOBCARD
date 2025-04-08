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
        List<Services> services = getServices();

        List<Work> works = getWorkDto(workDtos, services, jobCard);
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
        System.err.println("--------------------------------------------------------------------");

//        notifier.SpecialistMassageNotifier("Work added successfully");
//        notifier.JobCardNotifier(getAll(1));
//        notifier.TechnicianMassageNotifier("Work added successfully");
        return getWorkList(1);
    }


    public ResponseDto getByStatusNum(int status, int page, String search) {
        return jobService.getByStatusNum(status, page, search);
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
            throw new MyNotFoundException("service not found, may be Invalid service id");
        return optional;
    }


    private List<Work> getWorkDto(List<RequestWorkDto> workDtoList, List<Services> services, JobCard jobCard) {
        Map<String, Services> servicesMap = services.stream()
                .collect(Collectors.toMap(Services::getId, Function.identity()));
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


    public PdfFile getPdfFromJob(String jobId) {
        return jobService.getPdfFromJob(jobId);
    }
}
