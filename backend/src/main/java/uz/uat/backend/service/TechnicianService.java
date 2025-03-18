package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.RequestWorkDto;
import uz.uat.backend.dto.ResponseWorkDto;
import uz.uat.backend.mapper.TaskMapper;
import uz.uat.backend.model.Services;
import uz.uat.backend.model.Work;
import uz.uat.backend.repository.JobCarRepository;
import uz.uat.backend.repository.ServicesRepository;
import uz.uat.backend.repository.WorkRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final JobCarRepository jobCarRepository;
    private final WorkRepository workRepository;
    private final TaskMapper taskMapper;
    private final ServicesRepository servicesRepository;


    public List<ResponseWorkDto> addWork(RequestWorkDto workDto) {



        return getWorkList();
    }


    public void closedWork(RequestWorkDto workDto) {

    }

    private List<ResponseWorkDto> getWorkList() {
        List<Work> works = workRepository.getAll();
        if (works.isEmpty())
            throw new MyNotFoundException("work list is empty");
        return getWorkDto(works);
    }


    private List<ResponseWorkDto> getWorkDto(List<Work> workList) {
        List<ResponseWorkDto> workDtos = new ArrayList<>();

        Set<String> serviceIds = workList.stream()
                .map(work -> work.getService_id().getId())
                .collect(Collectors.toSet());

        Map<String, Services> serviceMap = servicesRepository.findAllById(serviceIds)
                .stream().collect(Collectors.toMap(Services::getId, service -> service));

        for (Work work : workList) {
            Services service = serviceMap.get(work.getService_id().getId());

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
                    .dit(work.isDit())
                    .workers_names(work.getWorkers_names())
                    .taskList(taskMapper.list(
                            service != null && service.getTasks() != null ? service.getTasks() : Collections.emptyList()
                    ))
                    .build()
            );
        }
        return workDtos;
    }

}
