package uz.uat.backend.service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.*;
import uz.uat.backend.mapper.TaskMapper;
import uz.uat.backend.model.*;
import uz.uat.backend.model.enums.OperationStatus;
import uz.uat.backend.model.enums.Status;
import uz.uat.backend.model.enums.TableName;
import uz.uat.backend.model.history_models.History;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.PDFfileRepository;
import uz.uat.backend.repository.ServicesRepository;
import uz.uat.backend.repository.WorkRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilsService {

    private final PDFfileRepository fileRepository;
    private final TaskMapper taskMapper;
    private final ServicesRepository servicesRepository;
    private final WorkRepository workRepository;
    private final JobCarRepository jobCarRepository;

    public List<ResponseServiceDto> fromEntityService(List<Services> services) {
        return services.stream()
                .map(service -> ResponseServiceDto.builder()
                        .id(service.getId())
                        .service_type(service.getServiceType())
                        .service_name(service.getServiceName().getId())
                        .revision_number(service.getRevisionNumber())
                        .revision_time(service.getRevisionTime())
                        .build())
                .toList();
    }

    public List<ResponseWorkDto> getWork(List<Work> workList) {
        List<ResponseWorkDto> workDtos = new ArrayList<>();
        for (Work work : workList) {
            workDtos.add(ResponseWorkDto.builder()
                    .jobCard_id(work.getService_id().getId())
                    .service_id(work.getService_id().getId())
                    .threshold(work.getThreshold())
                    .repeat_int(work.getRepeat_int())
                    .zone(work.getZone())
                    .mpr(work.getMpr())
                    .access(work.getAccess())
                    .airplane_app(work.getAirplane_app())
                    .description(work.getDescription())
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

    public ResponseServiceDto fromEntityService(Services service) {
        return ResponseServiceDto.builder()
                .id(service.getId())
                .service_type(service.getServiceType())
                .service_name(service.getServiceName().getId())
                .revision_number(service.getRevisionNumber())
                .revision_time(service.getRevisionTime())
                .build();

    }

    public List<ResponseService> getTasksFromService(List<Services> services) {
        return services.stream()
                .map(service -> ResponseService.builder()
                        .service(fromEntityService(List.of(service)))
                        .tasks(taskMapper.list(service.getTasks()) != null ? taskMapper.list(service.getTasks()) : new ArrayList<>())
                        .build())
                .toList();
    }

    public ResponseService getTaskFromService(Services service) {
        return ResponseService.builder()
                .service(fromEntityService(service))
                .tasks(taskMapper.list(service.getTasks()))
                .build();
    }

    public List<ResponseJobCardDto> getJobCards(List<JobCard> jobCards) {
        return jobCards.stream()
                .map(this::getJobCard)
                .toList();
    }

    public PdfFile getFile(JobCard jobCard) {
        if (jobCard.getMainPlan() == null)
            return new PdfFile();
        Optional<PdfFile> optional = fileRepository.findById(String.valueOf(jobCard.getMainPlan().getId()));
        if (optional.isEmpty())
            throw new MyNotFoundException("file not found");
        return optional.get();
    }

    public ResponseJobCardDto getJobCard(JobCard jobCard) {
        PdfFile file = getFile(jobCard);
        return ResponseJobCardDto.builder()
                .id(jobCard.getId())
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
                .status(String.valueOf(getStatus(jobCard.getStatus())))
                .is_file(file.getData() != null)
                .filename(file.getFileName())
                .build();
    }

    public int getStatus(Status status) {
        if (status != null) {
            switch (status) {
                case Status.NEW -> {
                    return 1;
                }
                case Status.PENDING -> {
                    return 2;
                }
                case Status.IN_PROCESS -> {
                    return 3;
                }
                case Status.CONFIRMED -> {
                    return 4;
                }
                case Status.COMPLETED -> {
                    return 5;
                }
                case Status.REJECTED -> {
                    return 6;
                }
            }
        }
        return 0;
    }

    public List<ResponseWorkDto> getWorksDto(List<Work> workList) {
        return workList.stream()
                .map(this::getWork)
                .toList();
    }

    public ResponseWorkDto getWork(Work work) {
        return ResponseWorkDto.builder()
                .threshold(work.getThreshold())
                .repeat_int(work.getRepeat_int())
                .zone(work.getZone())
                .mpr(work.getMpr())
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
                .build();

    }

    public ResultWorkDto getWork(Work work, Services service) {
        return ResultWorkDto.builder()
                .work_id(work.getId())
                .threshold(work.getThreshold())
                .repeat_int(work.getRepeat_int())
                .zone(work.getZone())
                .mpr(work.getMpr())
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
                .service(fromEntityService(service))
                .build();

    }

    public Services getServiceById(String id) {
        Optional<Services> optional = servicesRepository.findById(id);
        if (optional.isEmpty())
            throw new MyNotFoundException("work not found by this jobid: {}" + id);
        return optional.get();
    }

    public Work getWorkById(String id) {
        Optional<Work> optional = workRepository.findById(id);
        if (optional.isEmpty())
            throw new MyNotFoundException("work not found by this jobid: {}" + id);
        return optional.get();
    }

    public JobCard getJobById(String id) {
        JobCard jobCardId = jobCarRepository.findByJobCardId(id);
        if (jobCardId == null)
            throw new MyNotFoundException("job card not found, may be Invalid job card jobid");
        return jobCardId;
    }

    public List<ResponseHistoryDto> getHistorys(List<History> histories) {
        return histories.stream()
                .map(this::getHistory)
                .toList();
    }

    private ResponseHistoryDto getHistory(History history) {
        return ResponseHistoryDto.builder()
                .tablename(history.getTablename().name())
                .tableID(history.getTableID())
                .OS(history.getOS().name())
                .oldValue(history.getOldValue())
                .newValue(history.getNewValue())
                .updatedBy(history.getUpdatedBy())
                .updTime(history.getUpdTime().atZone(ZoneId.of("Asia/Tashkent")).toString())
                .build();
    }


}
