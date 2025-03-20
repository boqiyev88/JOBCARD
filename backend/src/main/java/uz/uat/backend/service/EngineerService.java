package uz.uat.backend.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.uat.backend.component.Notifier;
import uz.uat.backend.config.exception.MyConflictException;
import uz.uat.backend.config.exception.MyNotFoundException;
import uz.uat.backend.dto.HistoryDto;
import uz.uat.backend.dto.ResponseServiceDto;
import uz.uat.backend.dto.TaskDto;
import uz.uat.backend.dto.WorkListDto;
import uz.uat.backend.mapper.TaskMapper;
import uz.uat.backend.model.*;
import uz.uat.backend.repository.*;
import uz.uat.backend.service.serviceIMPL.EngineerServiceIM;


import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
@Service
@RequiredArgsConstructor
public class EngineerService implements EngineerServiceIM {

    private final ServicesRepository servicesRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceNameRepository serviceNameRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final Notifier notifier;
    private final HistoryService historyService;

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
    public List<TaskDto> uploadfile(@NotBlank MultipartFile file) {
        String contentType = file.getContentType();
        List<TaskDto> tasks;
        if (contentType.equals("application/pdf")) {
            tasks = uploadPDF(file);
        } else if (contentType.equals("text/csv")) {
            tasks = uploadCSV(file);
        } else
            throw new MyConflictException("Unsupported content type file: " + contentType);

        return tasks;
    }


    @Transactional
    @Override
    public List<ResponseServiceDto> addNewService(WorkListDto workListDto) {
        if (workListDto == null)
            throw new MyNotFoundException("workList is null");

        Optional<ServiceName> optional = serviceNameRepository.findByName(workListDto.serviceName_id());
        if (optional.isEmpty())
            throw new MyNotFoundException("service name not found");

        ServiceName serviceName = optional.get();
        List<Task> tasks = taskMapper.toEntitys(workListDto.tasks());
        List<Task> saved = taskRepository.saveAll(tasks);
        Services saveService = servicesRepository.save(
                Services.builder()
                        .serviceType(workListDto.serviceType())
                        .serviceName(serviceName)
                        .revisionTime(LocalDate.now())
                        .revisionNumber(workListDto.revisionNumber())
                        .tasks(saved)
                        .build()
        );
        historyService.addHistory(HistoryDto.builder()
                .tableID("Services table")
                .description("New Services added")
                .rowName(" ")
                .newValue(saveService.getId())
                .oldValue(" ")
                .updatedBy(saveService.getUpdUser())
                .updTime(saveService.getUpdTime())
                .build());
//        notifier.EngineerNotifier(saveService);
        return getMainManu();
    }


    @Override
    public List<ResponseServiceDto> getMainManu() {
        List<Services> services = servicesRepository.getAll();
        if (services.isEmpty())
            throw new MyNotFoundException("services not found");
        return fromEntity(services);
    }

    @Override
    public List<ServiceName> getServiceName() {
        Optional<List<ServiceName>> optionalList = serviceNameRepository.getServiceName();
        if (optionalList.isEmpty() || optionalList.get().isEmpty())
            throw new MyNotFoundException("serviceName is null");

        return optionalList.get();
    }


    @Override
    public List<ResponseServiceDto> search(LocalDate startDate, LocalDate endDate, String search) {
        if (search == null || search.isEmpty() && (startDate == null && endDate == null)) {
            throw new MyConflictException("all search parameters are null");
        } else if ((startDate == null && endDate == null)) {
            return getBySearch(search);

        } else if (startDate != null && endDate == null) {
            return getByDate(startDate, LocalDate.now());
        } else {
            return getByDate(startDate, endDate);
        }
    }


    private List<ResponseServiceDto> getByDate(LocalDate startDate, LocalDate endDate) {
        List<Services> services = servicesRepository.searchServicesByDate(startDate, endDate);
        if (services.isEmpty())
            throw new MyNotFoundException("services not found");
        return fromEntity(services);
    }

    private List<ResponseServiceDto> getBySearch(String search) {
        List<Services> services = servicesRepository.searchByNameOrType(search);
        if (services.isEmpty())
            getMainManu();
        return fromEntity(services);
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

    private List<ResponseServiceDto> fromEntity(List<Services> services) {
        List<ResponseServiceDto> rsd = new ArrayList<>();
        for (Services service : services) {
            rsd.add(ResponseServiceDto.builder()
                    .id(service.getId())
                    .service_type(service.getServiceType())
                    .service_name(service.getServiceName().getId())
                    .revisionNumber(service.getRevisionNumber())
                    .revisionTime(service.getRevisionTime())
                    .build()
            );
        }
        return rsd;
    }

    private List<TaskDto> uploadPDF(MultipartFile file) {
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

    private List<TaskDto> uploadCSV(MultipartFile file) {
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


}
