package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.dto.RequestWorkDto;
import uz.uat.backend.dto.ResponseWorkDto;
import uz.uat.backend.dto.Worker;
import uz.uat.backend.repository.EmployeeRepository;
import uz.uat.backend.model.Employeer;
import uz.uat.backend.model.JobCard;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.Work;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.ServicesRepository;
import uz.uat.backend.repository.WorkRepository;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final JobCarRepository jobCarRepository;
    private final WorkRepository workRepository;
    private final ServicesRepository servicesRepository;
    private final Notifier notifier;
    private final SpecialistService specialistService;
    private final HistoryService historyService;
    private final EmployeeRepository employeerRepository;


    @Transactional
    public List<ResponseWorkDto> addWork(RequestWorkDto workDto) {
        JobCard jobCard = getById(workDto.jobCard_id());
        Services service = getServiceById(workDto.service_id());
        Employeer employeer = getEmployeer(workDto.workers_names());
        Work work = getRequestWorkDto(workDto, service, jobCard, employeer);
        Work save1 = workRepository.save(work);
        jobCard.setStatus(Status.PENDING);
        JobCard save = jobCarRepository.save(jobCard);

        historyService.addHistory(HistoryDto.builder()
                .tableID("Work table")
                .description("New Work added")
                .rowName("")
                .oldValue("")
                .newValue(Status.NEW.name())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );

        historyService.addHistory(HistoryDto.builder()
                .tableID("Job Card table")
                .description("Job Card status updated")
                .rowName("Status")
                .oldValue(Status.NEW.name())
                .newValue(jobCard.getStatus().name())
                .updatedBy(save.getUpdUser())
                .updTime(Instant.now())
                .build()
        );


//        notifier.SpecialistMassageNotifier("Work added successfully");
//        notifier.JobCardNotifier(specialistService.getAll());
//        notifier.TechnicianMassageNotifier("Work added successfully");
        return getWorkList();
    }


    public void closedWork(RequestWorkDto workDto) {

    }

    public List<ResponseWorkDto> getWorkList() {
        List<Work> works = workRepository.getAll();
        if (works.isEmpty())
            throw new MyNotFoundException("work list is empty");
        return getWorkDto(works);
    }


    private JobCard getById(String id) {
        JobCard jobCardId = jobCarRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found, may be Invalid job card id");
        /// job card va workni biriktirilgan holda qaytish kerak
        return jobCardId;
    }

    private Services getServiceById(String id) {
        Optional<Services> optional = servicesRepository.findById(id);
        if (optional.isEmpty())
            throw new MyNotFoundException("service not found, may be Invalid service id");
        return optional.get();
    }


    private Work getRequestWorkDto(RequestWorkDto workDto, Services service, JobCard jobCard, Employeer workers) {
        return Work.builder()
                .service_id(service)
                .jobcard_id(jobCard)
                .threshold(workDto.threshold())
                .repeat_int(workDto.repeat_int())
                .zone(workDto.zone())
                .mrf(workDto.mrf())
                .access(workDto.access())
                .airplane_app(workDto.airplane_app())
                .access_note(workDto.access_note())
                .task_description(workDto.task_description())
                .dit(workDto.dit() ? 1 : 0)
                .workers_names(workers)
                .build();
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
                    .workers_names(work.getWorkers_names())
                    .build()
            );
        }
        return workDtos;
    }


    private Employeer getEmployeer(List<Worker> workers) {
        Employeer employeer = new Employeer();

        Map<String, Consumer<Employeer>> fieldMapping = Map.of(
                "avionic", e -> e.setAvionic(1),
                "mechanic", e -> e.setMechanic(1),
                "cab_mechanic", e -> e.setCab_mechanic(1),
                "sheet_metal", e -> e.setSheet_metal(1),
                "ndt", e -> e.setNdt(1)
        );

        for (Worker worker : workers) {
            if (worker.isChecked()) {
                fieldMapping.getOrDefault(worker.name(), e -> {
                }).accept(employeer);
            }
        }
        return employeerRepository.save(employeer);
    }


}
