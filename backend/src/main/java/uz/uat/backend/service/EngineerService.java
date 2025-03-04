package uz.uat.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.dto.ServiceDto;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.mapper.ServicesMapper;
import uz.uat.backend.model.*;
import uz.uat.backend.repository.*;
import uz.uat.backend.service.serviceIMPL.EngineerServiceIM;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EngineerService implements EngineerServiceIM {

    private final WorkTypeRepository workTypeRepository;
    private final WorkRepository workRepository;
    private final ServicesRepository servicesRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceNameRepository serviceNameRepository;
    private final TaskRepository taskRepository;
    private final ServicesMapper servicesMapper;

    public Resource generateCsvFile(String fileName) {
        try {
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Number,Description\n");
            for (int i = 1; i <= 1000; i++) {
                csvContent.append(i).append(".TASKNUM,Description ").append(i).append("\n");
            }

            // Faylni vaqtinchalik yaratish
            Path filePath = Files.createTempFile("tasks", ".csv");
            Files.write(filePath, csvContent.toString().getBytes());

            // Faylni yuklash uchun resurs yaratish
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Task> uploadCSV(MultipartFile file) {
        List<Task> tasks = new ArrayList<>();
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 2) {
                        tasks.add(
                                Task.builder()
                                        .Number(data[0])
                                        .description(data[1])
                                        .build()

                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public void addNewService(WorkListDto workListDto) {
        if (workListDto == null)
            throw new UsernameNotFoundException("workList is null");
        Optional<ServiceName> optional = serviceNameRepository.findByName(workListDto.serviceName());
        Optional<ServiceType> optional1 = serviceTypeRepository.findByName(workListDto.serviceType());

        if (optional1.isEmpty())
            throw new UsernameNotFoundException("serviceType is null");
        ServiceType serviceType = optional1.get();

        if (optional.isEmpty())
            throw new UsernameNotFoundException("service name not found");
        ServiceName serviceName = optional.get();
        try {
            servicesRepository.save(
                    Services.builder()
                            .serviceType(serviceType)
                            .serviceName(serviceName)
                            .revisionNumber(workListDto.revisionNumber())
                            .revisionTime(workListDto.revisionTime())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ServiceType> getServiceType() {
        Optional<List<ServiceType>> optional = serviceTypeRepository.getServiceType();
        if (optional.isEmpty() || optional.get().isEmpty())
            throw new UsernameNotFoundException("serviceType is null");

        return optional.get();
    }

    @Override
    public List<ServiceDto> getMainManu() {
        List<Services> services = servicesRepository.findAll();
        if (services.isEmpty())
            throw new UsernameNotFoundException("services is null");
        return servicesMapper.toDto(services);
    }

    @Override
    public List<ServiceName> getServiceName() {
        Optional<List<ServiceName>> optionalList = serviceNameRepository.getServiceName();
        if (optionalList.isEmpty() || optionalList.get().isEmpty())
            throw new UsernameNotFoundException("serviceName is null");

        return optionalList.get();
    }


    @Override
    public List<Work> searchByDate(LocalDateTime startDate, LocalDateTime endDate) {
//        List<Work> works = workRepository.searchWorkByDate(startDate, endDate);
//        if (works == null)
//            throw new UsernameNotFoundException("No work found by these dates");

        return null;
    }


    @Override
    public void getDeleteTask(String id) {
        servicesRepository.findById(id).ifPresent(services -> {
            servicesRepository.deleteById(id);
        });
        throw new UsernameNotFoundException("Delete service not found by this id " + id);
    }


    @Override
    public void editTask(String id, TaskDto taskDto) {

    }


}
