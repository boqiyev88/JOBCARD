package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.dto.RequestWorkDto;
import uz.uat.backend.dto.ResponseDto;
import uz.uat.backend.dto.ResponseWorkDto;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.model.enums.TableName;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.ServicesRepository;
import uz.uat.backend.repository.WorkRepository;

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
    private final SpecialistService specialistService;
    private final HistoryService historyService;


    @Transactional
    public ResponseDto addWork(List<RequestWorkDto> workDtos, String jobCard_id) {
        JobCard jobCard = getById(jobCard_id);
        List<Services> services = getServices();

        List<Work> works = getWorkDto(workDtos, services, jobCard);
        List<Work> saved = workRepository.saveAll(works);
        jobCard.setStatus(Status.PENDING);
        JobCard save = jobCarRepository.save(jobCard);

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.work.name())
                .tableID(" ")
                .description("New Work added")
                .rowName(" ")
                .oldValue(" ")
                .newValue(saved.toString())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );

        historyService.addHistory(HistoryDto.builder()
                .tablename(TableName.JOB.name())
                .tableID(jobCard.getId())
                .description("Job Card status updated")
                .rowName("Status")
                .oldValue(Status.NEW.name())
                .newValue(jobCard.getStatus().name())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );


        notifier.SpecialistMassageNotifier("Work added successfully");
        notifier.JobCardNotifier(specialistService.getAll(1));
        notifier.TechnicianMassageNotifier("Work added successfully");
        return getWorkList();
    }


    public void closedWork(RequestWorkDto workDto) {

    }

    public ResponseDto getWorkList() {
        Page<Work> works = workRepository.getAll(PageRequest.of(1, 10));
        if (works.isEmpty())
            throw new MyNotFoundException("work list is empty");
        return ResponseDto.builder()
                .page(1)
                .total(works.getTotalElements())
                .data(getWorkDto(works.getContent()))
                .build();
    }


    private JobCard getById(String id) {
        JobCard jobCardId = jobCarRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found, may be Invalid job card id");
        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
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
                        .mrf(dto.mrf())
                        .access(dto.access())
                        .airplane_app(dto.airplane_app())
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


    private List<ResponseWorkDto> getWorkDto(List<Work> workList) {
        List<ResponseWorkDto> workDtos = new ArrayList<>();
        for (Work work : workList) {
            workDtos.add(ResponseWorkDto.builder()
                    .jobCard_id(work.getService_id().getId())
                    .service_id(work.getService_id().getId())
                    .threshold(work.getThreshold())
                    .repeat_int(work.getRepeat_int())
                    .zone(work.getZone())
                    .mrf(work.getMrf())
                    .access(work.getAccess())
                    .airplane_app(work.getAirplane_app())
                    .access_note(work.getAccess_note())
                    .task_description(work.getTask_description())
                    .dit(work.getDit() == 1)
                    .avionic(work.getAvionic() == 1)
                    .mechanic(work.getMechanic() == 1)
                    .cab_mechanic(work.getCab_mechanic() == 1)
                    .sheet_metal(work.getSheet_metal() == 1)
                    .ndt(work.getNdt() == 1)
                    .build()
            );
        }
        return workDtos;
    }

}
