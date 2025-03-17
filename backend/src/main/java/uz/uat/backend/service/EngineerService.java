package uz.uat.backend.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.ResponseServiceDto;
import uz.uat.backend.dto.ServiceDto;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.mapper.ServicesMapper;
import uz.uat.backend.mapper.TaskMapper;
import uz.uat.backend.model.*;
import uz.uat.backend.repository.*;
import uz.uat.backend.service.serviceIMPL.EngineerServiceIM;


import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
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

    private final ServicesRepository servicesRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceNameRepository serviceNameRepository;
    private final TaskRepository taskRepository;
    private final ServicesMapper servicesMapper;
    private final TaskMapper taskMapper;
    private final Notifier notifier;

    public Resource generateCsvFile(@NotBlank String fileName) {
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
    public List<TaskDto> uploadPDF(MultipartFile file) {

        List<Task> taskList = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            String pdfText = new PDFTextStripper().getText(document);
            Pattern pattern = Pattern.compile("(\\d+)\\.\\s*(.+)");
            Matcher matcher = pattern.matcher(pdfText);
            while (matcher.find()) {
                String number = matcher.group(1);
                String text = matcher.group(2);
                taskList.add(
                        Task.builder()
                                .number(String.valueOf(number))
                                .description(text)
                                .build()
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskMapper.list(taskList);
    }


    @Override
    public List<TaskDto> uploadCSV(MultipartFile file) {
        List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
        List<TaskDto> tasksDto = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    tasks.add(
                            Task.builder()
                                    .number(data[0])
                                    .description(data[1])
                                    .build()

                    );
                }
            }
            taskRepository.saveAll(tasks);
            tasksDto = taskMapper.list(tasks);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasksDto;
    }

    @Override
    public void addNewService(WorkListDto workListDto) {
        if (workListDto == null)
            throw new MyNotFoundException("workList is null");
        Optional<ServiceName> optional = serviceNameRepository.findByName(workListDto.serviceName_id());
        Optional<ServiceType> optional1 = serviceTypeRepository.findByName(workListDto.serviceType_id());

        if (optional1.isEmpty() || optional.isEmpty())
            throw new MyNotFoundException("serviceType or serviceName not found by these ids");

        ServiceType serviceType = optional1.get();
        ServiceName serviceName = optional.get();

        Services saveService = servicesRepository.save(
                Services.builder()
                        .serviceType(serviceType)
                        .serviceName(serviceName)
                        .revisionNumber(workListDto.revisionNumber())
                        .revisionTime(workListDto.revisionTime())
                        .build()
        );
        notifier.EngineerNotifier(saveService);

    }

    @Override
    public List<ServiceType> getServiceType() {
        Optional<List<ServiceType>> optional = serviceTypeRepository.getServiceType();
        if (optional.isEmpty() || optional.get().isEmpty())
            throw new MyNotFoundException("serviceType is null");

        return optional.get();
    }

    @Override
    public List<ServiceDto> getMainManu() {
        List<Services> services = servicesRepository.findAll();
        if (services.isEmpty())
            throw new MyNotFoundException("services not found");
        return servicesMapper.toDto(services);
    }

    @Override
    public List<ServiceName> getServiceName() {
        Optional<List<ServiceName>> optionalList = serviceNameRepository.getServiceName();
        if (optionalList.isEmpty() || optionalList.get().isEmpty())
            throw new MyNotFoundException("serviceName is null");

        return optionalList.get();
    }


    @Override
    public List<ResponseServiceDto> searchByDate(@NotBlank LocalDateTime startDate, @NotBlank LocalDateTime endDate) {
        Optional<List<Services>> optional = servicesRepository.searchServicesByDate(startDate, endDate);
        if (optional.isEmpty() || optional.get().isEmpty())
            throw new MyNotFoundException("services is null");
        List<Services> services = optional.get();
        List<ResponseServiceDto> respDtoService = servicesMapper.fromDto(services);
        return respDtoService;
    }


    @Override
    public void getDeleteTask(@NotBlank String id) {
        Optional<Services> optional = servicesRepository.findById(id);
        if (optional.isEmpty())
            throw new MyNotFoundException("services not found by id " + id);
        Services service = optional.get();
        service.setIsDeleted(1);
        Services save = servicesRepository.save(service);
        notifier.EngineerNotifier(save);

    }


    @Override
    public void editTask(@NotBlank String id, @NotBlank TaskDto taskDto) {

    }


}
